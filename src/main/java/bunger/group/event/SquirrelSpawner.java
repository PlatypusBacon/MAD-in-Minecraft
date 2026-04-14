package bunger.group.event;

import bunger.group.data.StructureEventData;
import bunger.group.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;

import java.util.Random;

public class SquirrelSpawner {

    private static final Random RANDOM = new Random();
    private static final int SQUIRRELS_PER_DAY = 8;
    private static final int SPAWN_RADIUS = 256;

    public static void spawnNearStructure(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();

        for (int i = 0; i < SQUIRRELS_PER_DAY; i++) {
            int x = origin.getX() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int z = origin.getZ() + RANDOM.nextInt(SPAWN_RADIUS * 2) - SPAWN_RADIUS;
            int y = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE,
                    x, z);

            BlockPos spawnPos = new BlockPos(x, y, z);

            var squirrel = ModEntities.SQUIRREL.create(level);
            if (squirrel == null) continue;

            squirrel.moveTo(spawnPos, 0f, 0f);
            level.addFreshEntityWithPassengers(squirrel);
        }
    }
}