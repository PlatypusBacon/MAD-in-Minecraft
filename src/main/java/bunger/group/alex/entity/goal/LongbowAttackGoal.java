package bunger.group.alex.entity.goal;

import bunger.group.alex.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

import java.util.EnumSet;

public class LongbowAttackGoal<T extends Monster & RangedAttackMob> extends Goal {

    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public LongbowAttackGoal(T mob, double speedModifier, int attackIntervalMin, float attackRadius) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setMinAttackInterval(int ticks) {
        this.attackIntervalMin = ticks;
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && this.isHoldingLongbow();
    }

    protected boolean isHoldingLongbow() {
        return this.mob.isHolding(ModItems.LONGBOW);
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingLongbow();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            double targetDistSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            double dist = Math.sqrt(targetDistSqr);
            float pitchOffset = target.getY() < this.mob.getY() ? 0.0F : (float)(dist * 0.15F);

            boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(target);
            boolean hadLineOfSight = this.seeTime > 0;
            if (hasLineOfSight != hadLineOfSight) {
                this.seeTime = 0;
            }

            if (hasLineOfSight) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(targetDistSqr > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo((Entity) target, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double) this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.mob.getRandom().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (targetDistSqr > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (targetDistSqr < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                Entity vehicle = this.mob.getControlledVehicle();
                if (vehicle instanceof Mob mob) {
                    mob.lookAt(target, 30.0F, 30.0F);
                }
                this.mob.lookAt(target, 30.0F, 30.0F);
                this.mob.setXRot(this.mob.getXRot() - pitchOffset);
            } else {
                this.mob.getLookControl().setLookAt((Entity) target, 30.0F, 30.0F);
                this.mob.setXRot(this.mob.getXRot() - pitchOffset);
            }

            if (this.mob.isUsingItem()) {
                if (!hasLineOfSight && this.seeTime < -60) {
                    this.mob.stopUsingItem();
                } else if (hasLineOfSight) {
                    int pullTime = this.mob.getTicksUsingItem();
                    if (pullTime >= 45) {
                        this.mob.stopUsingItem();
                        ((RangedAttackMob) this.mob).performRangedAttack(target, BowItem.getPowerForTime(pullTime));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.mob, ModItems.LONGBOW));
            }
        }
    }
}