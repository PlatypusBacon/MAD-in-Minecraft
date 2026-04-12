package bunger.group.event;

import bunger.group.data.StructureEventData;
import bunger.group.structure.StructurePlacer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

public class StructureManager {

    public static void register() {
        registerEntryDetection();
        registerUnbreakableBlocks();
        registerUnplaceableBlocks();
        registerUnbreakableEntities();
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
                    onFirstEntry(level, player, data);
                    return;
                }
            }
        });
    }

    private static void onFirstEntry(ServerLevel level,
                                     ServerPlayer player,
                                     StructureEventData data) {
        data.setEntered();
        data.setSpawnpointLocked();

        // set spawnpoint immediately to the bed
        BlockPos bed = data.getBedPos();
        player.setRespawnPosition(
                level.dimension(),
                bed,
                0f,
                true,   // forced
                false   // no announcements
        );

        player.sendSystemMessage(
                Component.literal("§4You feel something watching you..."));
        player.sendSystemMessage(
                Component.literal("§7§oYour fate is sealed to this place."));
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

                    AABB bounds = getStructureBounds(data);
                    if (!bounds.contains(entity.getX(), entity.getY(), entity.getZ()))
                        return net.minecraft.world.InteractionResult.PASS;

                    // allow interacting with armor stand (right click)
                    // only block attacking (left click)
                    ((ServerPlayer) player).sendSystemMessage(
                            Component.literal("§7§oGod does not approve"));
                    return net.minecraft.world.InteractionResult.FAIL;
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
        BlockPos origin = data.getStructureOrigin();
        BlockPos end    = data.getStructureEnd();
        return new AABB(
                origin.getX(), origin.getY(), origin.getZ(),
                end.getX(),    end.getY(),    end.getZ()
        );
    }
}