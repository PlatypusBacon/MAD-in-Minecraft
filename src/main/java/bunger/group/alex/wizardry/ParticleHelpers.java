package bunger.group.alex.wizardry;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleHelpers {

    private static final CopyOnWriteArrayList<TimedEffect> ACTIVE_EFFECTS = new CopyOnWriteArrayList<>();

    private static class TimedEffect {
        int ticksLeft;
        Runnable action;

        TimedEffect(int ticksLeft, Runnable action) {
            this.ticksLeft = ticksLeft;
            this.action = action;
        }
    }

    public static void tick() {
        Iterator<TimedEffect> it = ACTIVE_EFFECTS.iterator();

        while (it.hasNext()) {
            TimedEffect effect = it.next();

            effect.action.run();
            effect.ticksLeft--;

            if (effect.ticksLeft <= 0) {
                ACTIVE_EFFECTS.remove(effect);
            }
        }
    }

    public static void runForTicks(int ticks, Runnable action) {
        ACTIVE_EFFECTS.add(new TimedEffect(ticks, action));
    }

    public static void spawnBeamParticles(Level level, Vec3 start, Vec3 end, SimpleParticleType particle) {
        Vec3 direction = end.subtract(start);
        double distance = direction.length();

        direction = direction.normalize();

        for (double i = 0; i < distance; i += 0.5) {
            Vec3 pos = start.add(direction.scale(i));

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(particle, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            }
        }
    }

    public static void spawnRingParticles(Level level, Vec3 centre, double radius, double offset, SimpleParticleType particle) {
        for (int i = 0; i < 10; i++) {
            double angle = 2 * Math.PI * offset * i / 10;

            double x = centre.x + radius * Math.cos(angle);
            double z = centre.z + radius * Math.sin(angle);
            double y = centre.y;

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(particle, x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }
}