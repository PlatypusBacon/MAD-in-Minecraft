package bunger.group.tyler.event.god;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;

public class SquirrelBearSpawner {

    public static void spawnNearStructure(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();

        // spawn 1-2 bears per day from day 3 onward, offset from origin
        int count = data.getEventDay() >= 4 ? 2 : 1;

        for (int i = 0; i < count; i++) {
            var bear = ModEntities.SQUIRREL_BEAR.create(level, EntitySpawnReason.LOAD);
            if (bear == null) continue;

            // spread them out so they don't stack
            double offsetX = (i == 0) ? 12 : -12;
            bear.setPos(
                    origin.getX() + offsetX,
                    origin.getY(),
                    origin.getZ() + 8
            );
            bear.setYRot(level.getRandom().nextFloat() * 360f);
            level.addFreshEntity(bear);
        }
    }
}