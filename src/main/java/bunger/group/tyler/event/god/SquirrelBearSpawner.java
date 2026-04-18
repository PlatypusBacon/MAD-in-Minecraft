package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;

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

            int y = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, x, z);

            var bear = ModEntities.SQUIRREL_BEAR.create(level, EntitySpawnReason.LOAD);
            if (bear == null) continue;

            bear.setPos(x, y, z);
            bear.setYRot(level.getRandom().nextFloat() * 360f);
            level.addFreshEntity(bear);
            spawned++;
        }
    }
}