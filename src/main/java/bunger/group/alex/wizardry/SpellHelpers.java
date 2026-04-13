package bunger.group.alex.wizardry;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpellHelpers {

    private static final CopyOnWriteArrayList<TimedTask> TASKS = new CopyOnWriteArrayList<>();

    private static class TimedTask {
        int ticksLeft;
        int interval; // 0 for once at end, 1 for every tick
        Runnable action;

        TimedTask(int ticksLeft, int interval, Runnable action) {
            this.ticksLeft = ticksLeft;
            this.interval = interval;
            this.action = action;
        }
    }

    public static void tick() {
        Iterator<TimedTask> it = TASKS.iterator();

        while (it.hasNext()) {
            TimedTask task = it.next();

            if (task.interval == 1) {
                //every tick run task
                task.action.run();
                task.ticksLeft--;

                if (task.ticksLeft <= 0) {
                    TASKS.remove(task);
                }
            } else if (task.interval == 0) {
                task.ticksLeft--;

                if (task.ticksLeft <= 0) {
                    task.action.run();
                    TASKS.remove(task);
                }
            }
        }
    }

    public static void runForTicks(int ticks, Runnable action) {
        TASKS.add(new TimedTask(ticks, 1, action));
    }

    public static void runAfterTicks(int ticks, Runnable action) {
        TASKS.add(new TimedTask(ticks, 0, action));
    }
}