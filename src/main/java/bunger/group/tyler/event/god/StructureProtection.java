package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class StructureProtection {

    public static void register() {
        // Prevent any player breaking blocks inside the structure bounds
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!(world instanceof ServerLevel level)) return true;
            StructureEventData data = StructureEventData.get(level);
            if (!data.hasBeenEntered() || data.isEventComplete()) return true;
            if (isInsideStructure(pos, data)) {
                player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§cThe structure cannot be broken during the event."));
                return false;
            }
            return true;
        });
    }

    /**
     * Call this from your explosion event hook to filter out blocks
     * inside the structure. Fabric exposes explosion affected blocks
     * via the ExplosionKnockbackCallback or through mixin — see below.
     */
    public static boolean shouldProtectFromExplosion(ServerLevel level, BlockPos pos) {
        StructureEventData data = StructureEventData.get(level);
        if (!data.hasBeenEntered() || data.isEventComplete()) return false;
        return isInsideStructure(pos, data);
    }

    private static boolean isInsideStructure(BlockPos pos, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();
        BlockPos end    = data.getStructureEnd();

        int minX = Math.min(origin.getX(), end.getX());
        int maxX = Math.max(origin.getX(), end.getX());
        int minY = Math.min(origin.getY(), end.getY());
        int maxY = Math.max(origin.getY(), end.getY());
        int minZ = Math.min(origin.getZ(), end.getZ());
        int maxZ = Math.max(origin.getZ(), end.getZ());

        return pos.getX() >= minX && pos.getX() <= maxX
                && pos.getY() >= minY && pos.getY() <= maxY
                && pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }
}