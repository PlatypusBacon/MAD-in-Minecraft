package bunger.group.event;

import bunger.group.data.StructureEventData;
import bunger.group.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class SquirrelBearSpawner {

    public static void spawnNearStructure(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();

        // spawn 1-2 bears per day from day 3 onward, offset from origin
        int count = data.getEventDay() >= 4 ? 2 : 1;

        for (int i = 0; i < count; i++) {
            var bear = ModEntities.SQUIRREL_BEAR.create(level);
            if (bear == null) continue;

            // spread them out so they don't stack
            double offsetX = (i == 0) ? 12 : -12;
            bear.moveTo(
                    origin.getX() + offsetX,
                    origin.getY(),
                    origin.getZ() + 8,
                    level.random.nextFloat() * 360f, 0f
            );
            level.addFreshEntity(bear);
        }
    }
}