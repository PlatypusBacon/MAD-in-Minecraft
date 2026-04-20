package bunger.group.tyler.event;


import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.event.god.God;
import bunger.group.tyler.event.god.SquirrelBearSpawner;
import bunger.group.tyler.event.god.SquirrelSpawner;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Comparator;

public class SundownWatcher {

    // sunset tick within a day
    private static final long SUNSET_TIME   = 11000;
    // for testing — set to 200 ticks (10 seconds) per "day"
    // change back to 24000 for production
    private static final long DAY_LENGTH    = 24000;
    private static boolean sundownTriggered = false;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerLevel level = server.overworld();
            StructureEventData data = StructureEventData.get(level);

            if (!data.hasBeenEntered() || data.isEventComplete()) return;
            long dayTime    = level.getOverworldClockTime() % DAY_LENGTH;
            long currentDay = level.getOverworldClockTime() / DAY_LENGTH;

            // detect day change — works whether player slept or time passed
            if (currentDay > data.getLastDayProcessed()) {
                onNewDay(level, data, currentDay);
            }

            // detect day 5 sunset
            if (data.getEventDay() >= 5
                    && dayTime >= SUNSET_TIME
                    && dayTime < SUNSET_TIME + 40
                    && !sundownTriggered) {
                sundownTriggered = true;
                onDay5Sundown(level, data);
            }
        });
    }

    public static void triggerDayChange(ServerLevel level,
                                        StructureEventData data,
                                        long currentDay) {
        if (currentDay > data.getLastDayProcessed()) {
            onNewDay(level, data, currentDay);
        }
    }

    private static void onNewDay(ServerLevel level,
                                 StructureEventData data,
                                 long currentDay) {
        data.advanceDay(currentDay);

        int day = data.getEventDay();
        // day 1 = painting 4, day 2 = painting 3 ... day 5 = painting 0
        int paintingIndex = Math.max(0, 5 - day);
        PaintingUpdater.updatePaintingToIndex(level, data, paintingIndex);
        SquirrelSpawner.spawnNearStructure(level, data);
        if (day == 4) {
            SquirrelBearSpawner.spawnNearStructure(level, data);
        }
        System.out.println("Day advanced to " + day
                + " | painting index: " + paintingIndex);
    }

    private static void onDay5Sundown(ServerLevel level, StructureEventData data) {
        BlockPos origin = data.getStructureOrigin();
        var nearest = level.players().stream()
                .min(Comparator.comparingDouble(a -> a.distanceToSqr(origin.getX(), origin.getY(), origin.getZ())));
        nearest.ifPresent(p ->
                God.start(level, origin, (net.minecraft.server.level.ServerPlayer) p));
    }
}