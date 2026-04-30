package bunger.group.tyler3.tools;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.entity.FollowerNode;
import bunger.group.tyler3.entity.PlatformEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class EntityAttachmentHelper {

    /**
     * Determines which face of the platform entity was clicked,
     * based on the hit position relative to entity centre.
     */
    public static Direction getFaceFromHit(Vec3 hitPos, Vec3 entityPos) {
        Vec3 relative = hitPos.subtract(entityPos);
        // Find which axis has the largest absolute component
        double ax = Math.abs(relative.x);
        double ay = Math.abs(relative.y);
        double az = Math.abs(relative.z);

        if (ax > ay && ax > az) {
            return relative.x > 0 ? Direction.EAST : Direction.WEST;
        } else if (ay > ax && ay > az) {
            return relative.y > 0 ? Direction.UP : Direction.DOWN;
        } else {
            return relative.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    /**
     * Apply goo to a platform face.
     * Stores on entity data only — chunk data is not touched here.
     */
    public static boolean applyGooToPlatformFace(PlatformEntity platform, Direction face) {
        EntityAttachmentData data = platform.getAttachmentData();
        if (data.hasPlatformFace(face)) return false; // already has goo

        data.addPlatformFace(face);
        MutuallyAssuredDestruction.LOGGER.info("[EntityAttachment] Applied goo to platform face {}", face);
        return true;
    }

    /**
     * Called when the platform starts moving.
     * Scans all attached blocks recursively, destroys any outside blocks
     * that would be pulled into occupied space, converts all valid
     * attached blocks to FollowerNodes in the entity data.
     *
     * Returns false if the platform cannot move (fully blocked).
     */
    public static boolean buildFollowerTree(PlatformEntity platform, ServerLevel level) {
        EntityAttachmentData data = platform.getAttachmentData();
        BlockPos platformPos = platform.blockPosition();

        // Map of offset -> BlockState for all blocks we want to bring along
        Map<Vec3i, BlockState> treeBlocks = new LinkedHashMap<>();
        // Map of offset -> goo faces for each block in the tree
        Map<Vec3i, Set<Direction>> treeGoo = new LinkedHashMap<>();

        // BFS from platform faces outward
        Queue<Vec3i> toScan = new ArrayDeque<>();
        Set<Vec3i> visited = new HashSet<>();
        visited.add(Vec3i.ZERO); // platform itself

        // Seed from platform's own goo faces
        // Instead of seeding from data.getPlatformFaces():
        for (Direction face : ChunkAttachmentHelper.getStickyFaces(level, platformPos)) {
            Vec3i offset = new Vec3i(face.getStepX(), face.getStepY(), face.getStepZ());
            if (!visited.contains(offset)) {
                toScan.add(offset);
                visited.add(offset);
            }
        }

        while (!toScan.isEmpty()) {
            Vec3i offset = toScan.poll();
            BlockPos worldPos = platformPos.offset(offset);
            BlockState state = level.getBlockState(worldPos);

            if (state.isAir()) continue;
            if (state.getPistonPushReaction() == PushReaction.BLOCK) {
                // Immovable block in chain — destroy it and remove goo
                MutuallyAssuredDestruction.LOGGER.info(
                        "[EntityAttachment] Immovable block at offset {} — destroying", offset);
                level.destroyBlock(worldPos, true);
                continue;
            }

            treeBlocks.put(offset, state);

            // Find goo faces on this block pointing to other blocks in or joining the tree
            Set<Direction> blockGoo = ChunkAttachmentHelper.getStickyFaces(level, worldPos);
            Set<Direction> validGoo = EnumSet.noneOf(Direction.class);

            for (Direction face : blockGoo) {
                Vec3i neighbourOffset = offset.offset(
                        face.getStepX(), face.getStepY(), face.getStepZ());
                BlockPos neighbourPos = platformPos.offset(neighbourOffset);
                BlockState neighbourState = level.getBlockState(neighbourPos);

                // Bilateral check
                if (!ChunkAttachmentHelper.hasAttachment(level, neighbourPos, face.getOpposite())) continue;

                validGoo.add(face);

                if (!visited.contains(neighbourOffset) && !neighbourState.isAir()) {
                    visited.add(neighbourOffset);
                    toScan.add(neighbourOffset);
                }
            }

            treeGoo.put(offset, validGoo);
        }

        // Write tree into entity data
        data.clear();
        // Re-add platform faces
        // (they were cleared — re-seed from original goo faces)
        for (Direction face : getPlatformFacesFromChunk(platform, level)) {
            data.addPlatformFace(face);
        }

        for (Map.Entry<Vec3i, BlockState> entry : treeBlocks.entrySet()) {
            Vec3i offset = entry.getKey();
            data.addFollower(offset, entry.getValue(),
                    treeGoo.getOrDefault(offset, EnumSet.noneOf(Direction.class)));
        }

        MutuallyAssuredDestruction.LOGGER.info("[EntityAttachment] Built follower tree: {} blocks",
                treeBlocks.size());
        return true;
    }

    /**
     * Converts all follower nodes into real PlatformEntity followers in the world.
     * Removes the real blocks from the world.
     */
    public static void spawnFollowerEntities(PlatformEntity platform, ServerLevel level) {
        EntityAttachmentData data = platform.getAttachmentData();
        BlockPos platformPos = platform.blockPosition();

        for (Map.Entry<Vec3i, FollowerNode> entry : data.getFollowers().entrySet()) {
            Vec3i offset = entry.getKey();
            FollowerNode node = entry.getValue();
            BlockPos worldPos = platformPos.offset(offset);

            // Remove real block
            level.setBlock(worldPos, Blocks.AIR.defaultBlockState(), 3);

            // Spawn follower entity
            PlatformEntity follower = bunger.group.tyler3.entity.ModEntities.PLATFORM
                    .create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (follower == null) continue;

            Vec3 followerPos = Vec3.atBottomCenterOf(worldPos).add(0, 0.5, 0);
            follower.setPos(followerPos);
            follower.setCarriedState(node.state);
            follower.setIsCarrier(false);
            follower.setCarrierUUID(platform.getUUID());
            follower.setOffsetFromCarrier(new Vec3(offset.getX(), offset.getY(), offset.getZ()));
            level.addFreshEntity(follower);

            MutuallyAssuredDestruction.LOGGER.info(
                    "[EntityAttachment] Spawned follower at offset {} state {}", offset, node.state);
        }
    }

    /**
     * Called when platform stops at a track centre.
     * Converts all follower entities back to real blocks.
     * Restores chunk attachment data between the landed blocks.
     */
    public static void landFollowers(PlatformEntity platform, ServerLevel level) {
        EntityAttachmentData data = platform.getAttachmentData();
        BlockPos platformPos = platform.blockPosition();

        // Find all follower entities
        List<PlatformEntity> followers = level.getEntitiesOfClass(
                PlatformEntity.class,
                platform.getBoundingBox().inflate(64),
                e -> !e.isCarrier() && platform.getUUID().equals(e.getCarrierUUID())
        );

        for (PlatformEntity follower : followers) {
            Vec3i offset = toVec3i(follower.getOffsetFromCarrier());
            BlockPos landPos = platformPos.offset(offset);

            // Place real block
            level.setBlock(landPos, follower.getCarriedState(), 3);
            follower.discard();

            MutuallyAssuredDestruction.LOGGER.info(
                    "[EntityAttachment] Landed follower at {} state {}", landPos, follower.getCarriedState());
        }

        // Restore chunk attachment data between landed blocks
        restoreChunkAttachments(platform, level, platformPos);
    }

    /**
     * Restores chunk-side goo data between all blocks in the tree
     * after they land, using the entity's stored goo face data.
     */
    private static void restoreChunkAttachments(PlatformEntity platform,
                                                ServerLevel level, BlockPos platformPos) {
        EntityAttachmentData data = platform.getAttachmentData();

        // Restore platform face → neighbour links
        for (Direction face : data.getPlatformFaces()) {
            BlockPos neighbourPos = platformPos.relative(face);
            Vec3i neighbourOffset = new Vec3i(face.getStepX(), face.getStepY(), face.getStepZ());
            if (data.hasFollower(neighbourOffset)) {
                ChunkAttachmentHelper.applyAttachmentOneFace(level, platformPos, face);
                ChunkAttachmentHelper.applyAttachmentOneFace(level, neighbourPos, face.getOpposite());
            }
        }

        // Restore block-to-block links within the tree
        for (Map.Entry<Vec3i, FollowerNode> entry : data.getFollowers().entrySet()) {
            Vec3i offset = entry.getKey();
            FollowerNode node = entry.getValue();
            BlockPos blockPos = platformPos.offset(offset);

            for (Direction face : node.gooFaces) {
                Vec3i neighbourOffset = offset.offset(
                        face.getStepX(), face.getStepY(), face.getStepZ());
                BlockPos neighbourPos = platformPos.offset(neighbourOffset);

                if (data.hasFollower(neighbourOffset) || neighbourOffset.equals(Vec3i.ZERO)) {
                    ChunkAttachmentHelper.applyAttachmentOneFace(level, blockPos, face);
                    ChunkAttachmentHelper.applyAttachmentOneFace(level, neighbourPos, face.getOpposite());
                }
            }
        }
    }

    public static com.mojang.datafixers.util.Pair<Vec3i, Vec3i> getExitsForShape(RailShape shape) {
        Vec3i n = Direction.NORTH.getUnitVec3i();
        Vec3i s = Direction.SOUTH.getUnitVec3i();
        Vec3i e = Direction.EAST.getUnitVec3i();
        Vec3i w = Direction.WEST.getUnitVec3i();
        return switch (shape) {
            case NORTH_SOUTH -> com.mojang.datafixers.util.Pair.of(n, s);
            case EAST_WEST   -> com.mojang.datafixers.util.Pair.of(w, e);
            case NORTH_EAST  -> com.mojang.datafixers.util.Pair.of(n, e);
            case NORTH_WEST  -> com.mojang.datafixers.util.Pair.of(n, w);
            case SOUTH_EAST  -> com.mojang.datafixers.util.Pair.of(s, e);
            case SOUTH_WEST  -> com.mojang.datafixers.util.Pair.of(s, w);
            default          -> null;
        };
    }
    private static Set<Direction> getPlatformFacesFromChunk(PlatformEntity platform, ServerLevel level) {
        // Platform faces are stored on entity data directly — just return current set
        return platform.getAttachmentData().getPlatformFaces();
    }

    private static Vec3i toVec3i(Vec3 v) {
        return new Vec3i((int) Math.round(v.x), (int) Math.round(v.y), (int) Math.round(v.z));
    }
}