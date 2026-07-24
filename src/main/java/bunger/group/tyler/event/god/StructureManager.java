package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.event.wife.Wife;
import bunger.group.tyler.structure.StructurePlacer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;


public class StructureManager {
    public static void register() {
        registerEntryDetection();
        registerUnbreakableBlocks();
        registerUnplaceableBlocks();
        registerUnbreakableEntities();
        registerFirePrevention();
    }

    // --- Entry detection ---
    private static void registerEntryDetection() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerLevel level = server.overworld();
            StructureEventData data = StructureEventData.get(level);

            if (data.isEventComplete()) return;

            // if bounds not set yet, try to find natural structure
            if (data.getStructureOrigin().equals(BlockPos.ZERO)) {
                // only check every 100 ticks for performance
                if (level.getGameTime() % 100 == 0) {
                    StructurePlacer.checkAndSaveNaturalGeneration(level);
                }
                return;
            }

            if (data.hasBeenEntered()) return;

            AABB bounds = getStructureBounds(data);

            // debug every 100 ticks
            if (level.getGameTime() % 100 == 0) {
                System.out.println("Bounds: " + data.getStructureOrigin()
                        + " -> " + data.getStructureEnd());
            }

            for (ServerPlayer player : level.players()) {
                if (bounds.contains(player.getX(), player.getY(), player.getZ())) {
                    if (!data.hasBeenEntered()) {
                        onFirstEntry(level, player, data);
                        return;
                    } else if (!data.isEventComplete() && !data.isEventPlayer(player.getUUID())) {
                        // additional player entered during event
                        data.addEventPlayer(player.getUUID());
                        data.setSpawnpointLocked();
                        BlockPos bed = data.getBedPos();
                        player.setRespawnPosition(
                                new ServerPlayer.RespawnConfig(
                                        LevelData.RespawnData.of(level.dimension(), bed, 0f, 0f),
                                        true
                                ),
                                false
                        );
                        player.sendSystemMessage(
                                Component.literal("§4You feel something watching you..."));
                        player.sendSystemMessage(
                                Component.literal("§7§oYour fate is sealed to this place."));
                    }
                }
            }
        });
    }

    private static void onFirstEntry(ServerLevel level,
                                     ServerPlayer player,
                                     StructureEventData data) {
        data.setEntered();
        data.setSpawnpointLocked();
        data.addEventPlayer(player.getUUID());

        BlockPos bed = data.getBedPos();
        player.setRespawnPosition(
                new ServerPlayer.RespawnConfig(
                        LevelData.RespawnData.of(level.dimension(), bed, 0f, 0f),
                        false
                ),
                false
        );

        player.sendSystemMessage(
                Component.literal("§4Welcome Home"));
        player.sendSystemMessage(
                Component.literal("§7§oCome inside and see your wife."));
    }

    // --- Unbreakable blocks ---
    private static void registerUnbreakableBlocks() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, entity) -> {
            if (level.isClientSide()) return true;

            ServerLevel serverLevel = (ServerLevel) level;
            StructureEventData data = StructureEventData.get(serverLevel);

            if (!data.hasBeenEntered() || data.isEventComplete()) return true;

            AABB bounds = getStructureBounds(data);
            if (bounds.contains(pos.getX(), pos.getY(), pos.getZ())) {
                ((ServerPlayer) player).sendSystemMessage(
                        Component.literal("§7§oGod does not approve"));
                return false; // cancel break
            }

            return true;
        });
    }
    private static void registerUnbreakableEntities() {
        net.fabricmc.fabric.api.event.player.AttackEntityCallback.EVENT.register(
                (player, level, hand, entity, hitResult) -> {
                    if (level.isClientSide())
                        return net.minecraft.world.InteractionResult.PASS;

                    ServerLevel serverLevel = (ServerLevel) level;
                    StructureEventData data = StructureEventData.get(serverLevel);

                    if (!data.hasBeenEntered() || data.isEventComplete())
                        return net.minecraft.world.InteractionResult.PASS;

                    AABB bounds = getStructureBounds(data).inflate(2.0);
                    if (bounds.contains(entity.getX(), entity.getY(), entity.getZ())) {
                        ((ServerPlayer) player).sendSystemMessage(
                                Component.literal("§7§oGod does not approve"));
                        return net.minecraft.world.InteractionResult.FAIL;
                    }

                    return net.minecraft.world.InteractionResult.PASS;
                });
    }


    private static void registerFirePrevention() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerLevel level = server.overworld();
            StructureEventData data = StructureEventData.get(level);

            if (!data.hasBeenEntered() || data.isEventComplete()) return;
            if (level.getGameTime() % 10 != 0) return; // check every 10 ticks

            AABB bounds = getStructureBounds(data);
            BlockPos origin = data.getStructureOrigin();
            BlockPos end    = data.getStructureEnd();

            // scan all blocks in structure for fire
            for (BlockPos pos : BlockPos.betweenClosed(origin, end)) {
                var state = level.getBlockState(pos);
                if (state.is(net.minecraft.world.level.block.Blocks.FIRE)
                        || state.is(net.minecraft.world.level.block.Blocks.SOUL_FIRE)) {
                    level.removeBlock(pos, false);
                }
            }
        });
    }
    private static void registerUnplaceableBlocks() {
        // block placement only
        net.fabricmc.fabric.api.event.player.UseBlockCallback.EVENT.register(
                (player, level, hand, hitResult) -> {
                    if (level.isClientSide())
                        return net.minecraft.world.InteractionResult.PASS;

                    ServerLevel serverLevel = (ServerLevel) level;
                    StructureEventData data = StructureEventData.get(serverLevel);

                    if (!data.hasBeenEntered() || data.isEventComplete())
                        return net.minecraft.world.InteractionResult.PASS;

                    BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());
                    AABB bounds = getStructureBounds(data);

                    if (!bounds.contains(pos.getX(), pos.getY(), pos.getZ()))
                        return net.minecraft.world.InteractionResult.PASS;

                    // only block if player is actually holding a placeable block item
                    net.minecraft.world.item.ItemStack held =
                            player.getItemInHand(hand);
                    if (held.getItem() instanceof net.minecraft.world.item.BlockItem) {
                        ((ServerPlayer) player).sendSystemMessage(
                                Component.literal("§7§oGod does not approve."));
                        return net.minecraft.world.InteractionResult.FAIL;
                    }

                    return net.minecraft.world.InteractionResult.PASS;
                });
    }
    // --- Spawnpoint lock ---
    // call this when player tries to set a new spawnpoint outside the structure
    public static boolean isSpawnpointLocked(ServerLevel level) {
        return StructureEventData.get(level).isSpawnpointLocked();
    }

    private static AABB getStructureBounds(StructureEventData data) {
        BlockPos a = data.getStructureOrigin();
        BlockPos b = data.getStructureEnd();

        double minX = Math.min(a.getX(), b.getX());
        double minY = Math.min(a.getY(), b.getY());
        double minZ = Math.min(a.getZ(), b.getZ());

        double maxX = Math.max(a.getX(), b.getX()) + 1;
        double maxY = Math.max(a.getY(), b.getY()) + 1;
        double maxZ = Math.max(a.getZ(), b.getZ()) + 1;

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}