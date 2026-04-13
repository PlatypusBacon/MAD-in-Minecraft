package bunger.group.structure;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.data.StructureEventData;
import bunger.group.event.TickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Comparator;
import java.util.Optional;

public class StructurePlacer {
    private static final int DOOR_OFFSET_X = 7;
    private static final int DOOR_OFFSET_Y = 1;
    private static final int DOOR_OFFSET_Z = 24;

    private static final int PAINTING_OFFSET_X = 6;
    private static final int PAINTING_OFFSET_Y = 2;
    private static final int PAINTING_OFFSET_Z = 24;

    private static final Direction PAINTING_FACING_NONE = Direction.NORTH;

    private static final int STRUCTURE_WIDTH  = 18;
    private static final int STRUCTURE_HEIGHT = 7;
    private static final int STRUCTURE_DEPTH  = 29;

    private static final int BED_OFFSET_X      = 19;
    private static final int BED_OFFSET_Y      = 1;
    private static final int BED_OFFSET_Z      = 6;

    public static void place(ServerLevel level, BlockPos pos, String structureName) {
        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation id = new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, structureName);
        Optional<StructureTemplate> template = manager.get(id);

        if (template.isEmpty()) {
            System.err.println("Structure not found: " + id);
            return;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings();
        template.get().placeInWorld(level, pos, pos, settings, level.random, 2);

        StructureEventData data = StructureEventData.get(level);
        data.setStructureRotation(Rotation.NONE);

        // command-placed is always NONE rotation — hardcoded offset is safe
        BlockPos paintingPos = pos.offset(PAINTING_OFFSET_X, PAINTING_OFFSET_Y, PAINTING_OFFSET_Z);
        data.setPaintingPos(paintingPos);

        savePositions(level, pos, Rotation.NONE);
    }

    public static BlockPos resolvePaintingPos(ServerLevel level, StructureEventData data) {
        if (data.hasPaintingPos()) return data.getPaintingPos();

        BlockPos min = data.getStructureOrigin();
        BlockPos max = data.getStructureEnd();

        var paintings = level.getEntitiesOfClass(
                net.minecraft.world.entity.decoration.Painting.class,
                new net.minecraft.world.phys.AABB(min, max).inflate(4.0)
        );

        if (!paintings.isEmpty()) {
            BlockPos bedPos = data.getBedPos();
            BlockPos found = paintings.stream()
                    .min(Comparator.comparingDouble(p ->
                            p.distanceToSqr(bedPos.getX(), bedPos.getY(), bedPos.getZ())))
                    .map(p -> p.blockPosition().relative(p.getDirection()))
                    .orElse(null);

            if (found != null) {
                data.setPaintingPos(found);
                System.out.println("Painting resolved lazily at: " + found);
                return found;
            }
        }

        System.err.println("Painting still not found — use /setstructure painting");
        return data.getPaintingPos();
    }

    public static BlockPos getDoorPos(ServerLevel level) {
        StructureEventData data = StructureEventData.get(level);
        BlockPos o = data.getTrueOrigin();
        return switch (data.getStructureRotation()) {
            case NONE                -> o.offset(  9, 1,  22);
            case CLOCKWISE_90        -> o.offset(-22, 1,   9);
            case CLOCKWISE_180       -> o.offset( -9, 1, -22);
            case COUNTERCLOCKWISE_90 -> o.offset( 22, 1,  -9);
        };
    }

    private static void schedulePaintingPlacement(ServerLevel level,
                                                  BlockPos origin,
                                                  Rotation rotation,
                                                  BlockPos bedPos) {
        TickScheduler.schedule(level, level.getGameTime() + 20L, () -> {
            StructureEventData data = StructureEventData.get(level);
            BlockPos o = data.getTrueOrigin();

            BlockPos wallBlock = switch (rotation) {
                case NONE                -> o.offset(  6, 1,  24);
                case CLOCKWISE_90        -> o.offset(-24, 1,   6);
                case CLOCKWISE_180       -> o.offset( -6, 1, -24);
                case COUNTERCLOCKWISE_90 -> o.offset( 24, 1,  -6);
            };

            Direction facing = switch (rotation) {
                case NONE                -> Direction.NORTH;
                case CLOCKWISE_90        -> Direction.WEST;
                case CLOCKWISE_180       -> Direction.SOUTH;
                case COUNTERCLOCKWISE_90 -> Direction.EAST;
            };

            // discard existing, place fresh
            var existing = level.getEntitiesOfClass(
                    net.minecraft.world.entity.decoration.Painting.class,
                    new net.minecraft.world.phys.AABB(
                            data.getStructureOrigin(), data.getStructureEnd()).inflate(4.0)
            );
            existing.forEach(net.minecraft.world.entity.Entity::discard);

            var registry = level.registryAccess()
                    .registryOrThrow(Registry.PAINTING_VARIANT_REGISTRY);
            var variantHolder = registry.getHolder(ResourceKey.create(
                    Registry.PAINTING_VARIANT_REGISTRY,
                    new ResourceLocation("mutually-assured-destruction", "god_coming_4")));

            if (variantHolder.isEmpty()) {
                System.err.println("god_coming_4 variant not found");
                return;
            }

            var painting = new net.minecraft.world.entity.decoration.Painting(
                    level, wallBlock, facing, variantHolder.get());
            level.addFreshEntity(painting);

            BlockPos facePos = wallBlock.relative(facing);
            data.setPaintingPos(facePos);
            System.out.println("Painting placed: wall=" + wallBlock
                    + " facing=" + facing + " stored=" + facePos);
        });
    }

    public static void checkAndSaveNaturalGeneration(ServerLevel level) {
        ResourceLocation structureId = new ResourceLocation(
                MutuallyAssuredDestruction.MOD_ID, "squirrel_house");

        for (ServerPlayer player : level.players()) {
            BlockPos playerPos = player.blockPosition();

            var registry = level.registryAccess()
                    .registryOrThrow(Registry.STRUCTURE_REGISTRY);
            var structureKey = ResourceKey.create(Registry.STRUCTURE_REGISTRY, structureId);
            if (!registry.containsKey(structureKey)) continue;

            var structureStart = level.structureManager()
                    .getStructureAt(playerPos, registry.getOrThrow(structureKey));
            if (!structureStart.isValid()) continue;

            var pieces = structureStart.getPieces();
            if (pieces.isEmpty()) continue;

            Rotation rotation = Rotation.NONE;
            if (pieces.get(0) instanceof net.minecraft.world.level.levelgen
                    .structure.PoolElementStructurePiece poolPiece) {
                rotation = poolPiece.getRotation();
            }

            BlockPos origin = getTrueOriginFromBoundingBox(
                    structureStart.getBoundingBox(), rotation);

            StructureEventData data = StructureEventData.get(level);
            if (data.getStructureOrigin().equals(BlockPos.ZERO)) {
                System.out.println("Found squirrel house at: " + origin
                        + " rotation: " + rotation);
                data.setTrueOrigin(origin);
                data.setStructureRotation(rotation);
                BoundingBox bb = structureStart.getBoundingBox();
                System.out.println("BoundingBox: min=("
                        + bb.minX() + "," + bb.minY() + "," + bb.minZ()
                        + ") max=("
                        + bb.maxX() + "," + bb.maxY() + "," + bb.maxZ()
                        + ") rotation=" + rotation
                        + " derivedOrigin=" + origin);
                savePositions(level, origin, rotation);
            }
            return;
        }
    }

    public static void savePositions(ServerLevel level, BlockPos origin, Rotation rotation) {
        StructureEventData data = StructureEventData.get(level);

        data.setTrueOrigin(origin);

        BlockPos end = getRotatedEnd(origin, rotation);
        BlockPos minPos = new BlockPos(
                Math.min(origin.getX(), end.getX()),
                Math.min(origin.getY(), end.getY()),
                Math.min(origin.getZ(), end.getZ())
        );
        BlockPos maxPos = new BlockPos(
                Math.max(origin.getX(), end.getX()),
                Math.max(origin.getY(), end.getY()),
                Math.max(origin.getZ(), end.getZ())
        );
        data.setStructureBounds(minPos, maxPos);

        BlockPos bedPos = findBed(level, minPos, maxPos);
        if (bedPos != null) {
            data.setBedPos(bedPos);
            System.out.println("Bed found at: " + bedPos);
        } else {
            System.err.println("Bed not found — using rotated offsets");
            data.setBedPos(origin.offset(rotateOffset(
                    BED_OFFSET_X, BED_OFFSET_Y, BED_OFFSET_Z,
                    rotation, STRUCTURE_WIDTH, STRUCTURE_DEPTH)));
        }

        // always schedule painting placement — handles both surviving and
        // missing painting entities reliably regardless of rotation
        schedulePaintingPlacement(level, origin, rotation, data.getBedPos());

        // --- DEBUG: verify bounds, door, and painting positions ---
        BlockPos expectedDoor = getDoorPos(level);
        System.out.println("[DEBUG] Rotation: " + rotation);
        System.out.println("[DEBUG] TrueOrigin: " + origin);
        System.out.println("[DEBUG] Structure extends from " + origin + " to " + end);
        System.out.println("[DEBUG] Bounds (min/max): " + minPos + " -> " + maxPos);
        System.out.println("[DEBUG] Bed: " + data.getBedPos());
        System.out.println("[DEBUG] Expected door: " + expectedDoor
                + " = " + level.getBlockState(expectedDoor).getBlock());
        System.out.println("[DEBUG] Expected door top: " + expectedDoor.above()
                + " = " + level.getBlockState(expectedDoor.above()).getBlock());
        // Painting pos is set async in schedulePaintingPlacement, logged there
        // ------------------------------------------------------------------

        System.out.println("Bounds: " + minPos + " -> " + maxPos);
        System.out.println("Bed: " + data.getBedPos());
        System.out.println("Door (rotated): " + getDoorPos(level));
    }

    public static void savePositions(ServerLevel level, BlockPos origin) {
        savePositions(level, origin, Rotation.NONE);
    }

    private static Direction rotateFacing(Direction base, Rotation rotation) {
        return switch (rotation) {
            case NONE              -> base;
            case CLOCKWISE_90      -> base.getClockWise();
            case CLOCKWISE_180     -> base.getOpposite();
            case COUNTERCLOCKWISE_90 -> base.getCounterClockWise();
        };
    }

    private static BlockPos getTrueOriginFromBoundingBox(BoundingBox box, Rotation rotation) {
        return switch (rotation) {
            // NONE: origin is at minX/minZ corner
            case NONE                -> new BlockPos(box.minX(), box.minY(), box.minZ());
            // CW90: structure extends -X/+Z from origin → origin is at maxX/minZ
            case CLOCKWISE_90        -> new BlockPos(box.maxX(), box.minY(), box.minZ());
            // CW180: structure extends -X/-Z from origin → origin is at maxX/maxZ
            case CLOCKWISE_180       -> new BlockPos(box.maxX(), box.minY(), box.maxZ());
            // CCW90: structure extends +X/-Z from origin → origin is at minX/maxZ
            case COUNTERCLOCKWISE_90 -> new BlockPos(box.minX(), box.minY(), box.maxZ());
        };
    }

    private static BlockPos getRotatedEnd(BlockPos origin, Rotation rotation) {
        return switch (rotation) {
            // NONE: extends +X, +Z
            case NONE                -> origin.offset( STRUCTURE_WIDTH,  STRUCTURE_HEIGHT,  STRUCTURE_DEPTH);
            // CW90: origin at maxX/minZ → extends -X, +Z
            case CLOCKWISE_90        -> origin.offset(-STRUCTURE_DEPTH,  STRUCTURE_HEIGHT,  STRUCTURE_WIDTH);
            // CW180: origin at maxX/maxZ → extends -X, -Z
            case CLOCKWISE_180       -> origin.offset(-STRUCTURE_WIDTH,  STRUCTURE_HEIGHT, -STRUCTURE_DEPTH);
            // CCW90: origin at minX/maxZ → extends +X, -Z
            case COUNTERCLOCKWISE_90 -> origin.offset( STRUCTURE_DEPTH,  STRUCTURE_HEIGHT, -STRUCTURE_WIDTH);
        };
    }

    private static BlockPos findBed(ServerLevel level, BlockPos min, BlockPos max) {
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof net.minecraft.world.level.block.BedBlock) {
                if (state.getValue(net.minecraft.world.level.block.BedBlock.PART)
                        == net.minecraft.world.level.block.state.properties.BedPart.HEAD) {
                    return pos.immutable();
                }
            }
        }
        return null;
    }

    private static BlockPos rotateOffset(int x, int y, int z, Rotation rotation,
                                         int width, int depth) {
        return switch (rotation) {
            case NONE                -> new BlockPos(x, y, z);
            case CLOCKWISE_90        -> new BlockPos(z, y, depth - 1 - x);
            case CLOCKWISE_180       -> new BlockPos(width - 1 - x, y, depth - 1 - z);
            case COUNTERCLOCKWISE_90 -> new BlockPos(width - 1 - z, y, x);
        };
    }
}