package bunger.group.tyler3.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class DudeGoal extends MeleeAttackGoal {

    private final DudeEntity dude;
    private int jumpCooldown = 0;

    public DudeGoal(DudeEntity dude) {
        super(dude, 1.4, false);
        this.dude = dude;
    }

    @Override
    public boolean canUse() {
        return dude.getTarget() != null && dude.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return dude.getTarget() != null && dude.getTarget().isAlive() && dude.isInCombat();
    }

    @Override
    public void start() {
        super.start();
        dude.setCombatEquipment();
        jumpCooldown = 0;
    }

    @Override
    public void stop() {
        super.stop();
        dude.setIdleEquipment();
        jumpCooldown = 0;
        dude.setSprinting(false);
    }

    @Override
    public void tick() {
        LivingEntity target = dude.getTarget();
        if (target == null) return;

        super.tick();

        double distanceSqr = dude.distanceToSqr(target);
        boolean inChaseRange = distanceSqr > 4.0 && distanceSqr < 1024.0;

        if (jumpCooldown > 0) jumpCooldown--;

        if (inChaseRange) {
            dude.setSprinting(true);

            // Compute flat direction toward target
            double dx = target.getX() - dude.getX();
            double dz = target.getZ() - dude.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);
            double nx = dx / dist;
            double nz = dz / dist;

            if (dude.onGround() && jumpCooldown == 0) {
                // Lock yaw to target before jumping so air travel is straight
                float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx))) - 90.0f;
                dude.setYRot(yaw);
                dude.yBodyRot = yaw;
                dude.yHeadRot = yaw;

                float radians = yaw * ((float)Math.PI / 180F);
                dude.setDeltaMovement(
                        -Mth.sin(radians) * 0.6,
                        0.42,
                        Mth.cos(radians) * 0.6
                );

                jumpCooldown = 12;
            } else if (!dude.onGround()) {
                // While airborne, gently steer without fighting momentum
                var vel = dude.getDeltaMovement();
                dude.setDeltaMovement(
                        vel.x * 0.91 + nx * 0.08,
                        vel.y,
                        vel.z * 0.91 + nz * 0.08
                );
            }
        } else {
            dude.setSprinting(false);
        }
    }
}