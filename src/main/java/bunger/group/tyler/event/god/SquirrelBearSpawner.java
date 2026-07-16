package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.levelgen.Heightmap;

public class SquirrelBearSpawner {

    private static final java.util.Random RANDOM = new java.util.Random();
    private static final int SPAWN_RADIUS = 64;

    public static void spawnNearStructure(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();
        BlockPos end = data.getStructureEnd();

        int minX = Math.min(origin.getX(), end.getX());
        int maxX = Math.max(origin.getX(), end.getX());
        int minZ = Math.min(origin.getZ(), end.getZ());
        int maxZ = Math.max(origin.getZ(), end.getZ());

        int count = data.getEventDay() >= 4 ? 2 : 1;

        int spawned = 0;
        int attempts = 0;
        while (spawned < count && attempts < 100) {
            attempts++;

            int x = origin.getX() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = origin.getZ() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;

            // Reject if inside structure bounds
            if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) continue;

            // Ignores leaves, so we don't land on top of tree canopies
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

            BlockPos spawnPos = new BlockPos(x, y, z);
            if (!isValidGroundSpawn(level, spawnPos)) continue;

            var bear = ModEntities.SQUIRREL_BEAR.create(level, EntitySpawnReason.LOAD);
            if (bear == null) continue;

            bear.setPos(x, y, z);
            bear.setYRot(level.getRandom().nextFloat() * 360f);
            level.addFreshEntity(bear);
            spawned++;
        }
        System.out.println("[SquirrelBearSpawner] Done: spawned " + spawned
                + " squirrel bears in " + attempts + " attempts");
    }

    /**
     * Rejects spawn points that are in/on a fluid, or have nothing solid underneath
     */
    private static boolean isValidGroundSpawn(ServerLevel level, BlockPos pos) {
        BlockPos below = pos.below();

        // The spawn block itself is water/lava
        if (!level.getFluidState(pos).isEmpty()) return false;

        // The block we'd be "standing on" is actually a fluid surface
        if (!level.getFluidState(below).isEmpty()) return false;

        // Nothing solid underneath -> would float/fall
        if (level.getBlockState(below).isAir()) return false;

        return true;
    }
}