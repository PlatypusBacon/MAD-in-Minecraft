package bunger.group.tyler3.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class DeerBeingFunny<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private final Mob mob;

    public DeerBeingFunny(PathfinderMob mob, Class<T> targetType, boolean mustSee)
    {
        super(mob, targetType, mustSee);
        this.mob = mob;
    }

    @Override
    public boolean canUse()
    {
        return mob.level().isDarkOutside() && super.canUse();
    }

    @Override
    public boolean canContinueToUse()
    {
        return mob.level().isDarkOutside() && super.canContinueToUse();
    }
}