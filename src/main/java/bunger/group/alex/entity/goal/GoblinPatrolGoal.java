package bunger.group.alex.entity.goal;

import bunger.group.alex.entity.goblin.GoblinChiefEntity;
import bunger.group.alex.entity.goblin.GoblinPatrolMember;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GoblinPatrolGoal<T extends PathfinderMob & GoblinPatrolMember> extends Goal {

    private static final double FOLLOW_DIST_SQ = 20.0 * 20.0;
    private static final double ARRIVE_DIST_SQ  = 4.0 * 4.0;
    private static final double WANDER_RADIUS   = 6.0;

    private final T mob;
    private final double speed;

    public GoblinPatrolGoal(T mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null) return false;
        GoblinChiefEntity patrol = mob.getPatrol();
        if (patrol == null || !patrol.isAlive()) return false;
        return mob.distanceToSqr(patrol) > FOLLOW_DIST_SQ;
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.getTarget() != null) return false;
        GoblinChiefEntity patrol = mob.getPatrol();
        if (patrol == null || !patrol.isAlive()) return false;
        return mob.distanceToSqr(patrol) > ARRIVE_DIST_SQ;
    }

    @Override
    public void start() {
        moveToNearChief();
    }

    @Override
    public void tick() {
        // Re-path every 40 ticks toward a random spot near the chief
        if (mob.tickCount % 40 == 0) {
            moveToNearChief();
        }
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    private void moveToNearChief() {
        GoblinChiefEntity patrol = mob.getPatrol();
        if (patrol == null) return;

        // Pick a random offset around the chief so they mill around rather than stack
        double angle = mob.getRandom().nextDouble() * Math.PI * 2.0;
        double dist  = mob.getRandom().nextDouble() * WANDER_RADIUS;
        double tx = patrol.getX() + Math.cos(angle) * dist;
        double ty = patrol.getY();
        double tz = patrol.getZ() + Math.sin(angle) * dist;

        mob.getNavigation().moveTo(tx, ty, tz, speed);
    }
}