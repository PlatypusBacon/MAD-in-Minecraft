package bunger.group.tyler3.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class DeerBeingSerious<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final Mob mob;

    public DeerBeingSerious(PathfinderMob mob, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
        super(mob, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
        this.mob = mob;
    }

    @Override
    public boolean canUse()
    {
        return mob.level().isBrightOutside() && super.canUse();
    }
}
