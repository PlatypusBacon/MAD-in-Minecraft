package bunger.group.alex.wizardry;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpellHelpers {

    private static final CopyOnWriteArrayList<TimedTask> ACTIVE_TASKS = new CopyOnWriteArrayList<>();

    private static class TimedTask {
        int ticksLeft;
        Runnable action;

        TimedTask(int ticksLeft, Runnable action) {
            this.ticksLeft = ticksLeft;
            this.action = action;
        }
    }

    public static void tick() {
        Iterator<TimedTask> it = ACTIVE_TASKS.iterator();

        while (it.hasNext()) {
            TimedTask task = it.next();

            task.action.run();
            task.ticksLeft--;

            if (task.ticksLeft <= 0) {
                ACTIVE_TASKS.remove(task);
            }
        }
    }

    public static void runForTicks(int ticks, Runnable action) {
        ACTIVE_TASKS.add(new TimedTask(ticks, action));
    }
}