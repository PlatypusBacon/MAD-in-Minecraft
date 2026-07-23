package bunger.group.tyler3.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class DeerGetingFunny extends MeleeAttackGoal {
    private final Mob mob;

    public DeerGetingFunny(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
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
