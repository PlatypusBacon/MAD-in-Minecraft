package bunger.group.tyler.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class TickScheduler {

    private static final List<AbstractMap.SimpleEntry<Long, Runnable>> SCHEDULED
            = new ArrayList<>();

    public static void schedule(ServerLevel level, long targetTick, Runnable task) {
        SCHEDULED.add(new AbstractMap.SimpleEntry<>(targetTick, task));
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level == null) return;
            long now = level.getGameTime();

            // snapshot to avoid ConcurrentModificationException when tasks add new entries
            List<AbstractMap.SimpleEntry<Long, Runnable>> snapshot = new ArrayList<>(SCHEDULED);
            SCHEDULED.clear();

            for (var entry : snapshot) {
                if (now >= entry.getKey()) {
                    entry.getValue().run(); // may add to SCHEDULED safely now
                } else {
                    SCHEDULED.add(entry); // not yet due, keep it
                }
            }
        });
    }
}