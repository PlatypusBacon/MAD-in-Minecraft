package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;

import java.util.Random;

public class SquirrelSpawner {

    private static final Random RANDOM = new Random();
    private static final int SQUIRRELS_PER_DAY = 8;
    private static final int SPAWN_RADIUS = 256;

    public static void spawnNearStructure(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();
        BlockPos end = data.getStructureEnd();

        // Structure bounds to avoid spawning inside
        int minX = Math.min(origin.getX(), end.getX());
        int maxX = Math.max(origin.getX(), end.getX());
        int minZ = Math.min(origin.getZ(), end.getZ());
        int maxZ = Math.max(origin.getZ(), end.getZ());

        int spawned = 0;
        int attempts = 0;
        while (spawned < SQUIRRELS_PER_DAY && attempts < 100) {
            attempts++;

            int x = origin.getX() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = origin.getZ() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;

            // Reject if inside structure bounds
            if (x >= minX && x <= maxX && z >= minZ && z <= maxZ) continue;

            int y = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, x, z);

            var squirrel = ModEntities.SQUIRREL.create(level, EntitySpawnReason.LOAD);
            if (squirrel == null) continue;

            squirrel.setPos(x, y, z);
            level.addFreshEntityWithPassengers(squirrel);
            spawned++;
        }
    }
}