package bunger.group.alex.entity.goal;

import bunger.group.alex.entity.goblin.GoblinChiefEntity;
import bunger.group.alex.entity.goblin.GoblinPatrolMember;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * When a goblin has no attack target, walk toward the patrol anchor.
 * Drops out the moment the goblin acquires a combat target.
 */
public class GoblinPatrolGoal<T extends PathfinderMob & GoblinPatrolMember> extends Goal {

    private static final double FOLLOW_DIST_SQ = 15.0 * 15.0;
    private static final double ARRIVE_DIST_SQ  = 2.0 * 2.0;

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
        GoblinChiefEntity patrol = mob.getPatrol();
        if (patrol != null) mob.getNavigation().moveTo(patrol, speed);
    }

    @Override
    public void tick() {
        GoblinChiefEntity patrol = mob.getPatrol();
        if (patrol == null) return;
        if (mob.tickCount % 20 == 0) {
            mob.getNavigation().moveTo(patrol, speed);
        }
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }
}