package bunger.group.alex.entity.goal;

import bunger.group.alex.entity.MageMob;
import bunger.group.alex.item.spell.SpellTemplate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MediumCastGoal extends Goal {

    private static final int WARMUP_TICKS   = 10;
    private static final int LOCKED_TICKS   = 5;
    private static final int TOTAL_CAST     = WARMUP_TICKS + LOCKED_TICKS;
    private static final int COOLDOWN_TICKS = 60;

    private final MageMob mage;
    private final double speedModifier;
    private LivingEntity target;
    private int castTimer;
    private int nextCastTick;

    // Strafing state
    private int strafeTimer;
    private float strafeX; // sideways: -1 left, +1 right
    private float strafeZ; // forward/back: -1 back, +1 forward
    private boolean hasCast;

    public MediumCastGoal(MageMob mage, double speed) {
        this.mage = mage;
        this.speedModifier = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private SpellTemplate getSpell() {
        ItemStack held = mage.getItemBySlot(EquipmentSlot.MAINHAND);
        return held.getItem() instanceof SpellTemplate s ? s : null;
    }

    @Override
    public boolean canUse() {
        target = mage.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (mage.tickCount < nextCastTick) return false;
        return getSpell() != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null || !target.isAlive() || getSpell() == null) return false;
        return true; // goal never self-exits, stop() only called when target dies or spell gone
    }

    @Override
    public void start() {
        castTimer = 0;
        hasCast = false;
        strafeTimer = 0;
        strafeX = 0;
        strafeZ = 0;
        mage.setCastTarget(null);
    }

    @Override
    public void stop() {
        nextCastTick = mage.tickCount + COOLDOWN_TICKS;
        mage.setCastTarget(null);
        castTimer = 0;
        hasCast = false;
        mage.getNavigation().stop();
        mage.getMoveControl().setWantedPosition(mage.getX(), mage.getY(), mage.getZ(), 0);
    }

    private void pickNewStrafe(double dist, double castRange) {
        strafeTimer = 15 + mage.getRandom().nextInt(15); // slightly snappier direction changes

        strafeX = mage.getRandom().nextBoolean() ? 1f : -1f;

        if (dist > castRange) {
            strafeZ = 1f; // full forward when approaching
        } else if (dist < castRange * 0.4) {
            strafeZ = -1f; // full back when too close
        } else {
            strafeZ = 0f;
        }
    }

    private void applyStrafe() {
        double dx = target.getX() - mage.getX();
        double dz = target.getZ() - mage.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.001) return;

        double fwdX = dx / len;
        double fwdZ = dz / len;
        double rightX =  fwdZ;
        double rightZ = -fwdX;

        double moveX = fwdX * strafeZ + rightX * strafeX;
        double moveZ = fwdZ * strafeZ + rightZ * strafeX;

        mage.getMoveControl().setWantedPosition(
                mage.getX() + moveX,
                mage.getY(),
                mage.getZ() + moveZ,
                speedModifier * 1.6
        );
    }

    @Override
    public void tick() {
        SpellTemplate spell = getSpell();
        if (spell == null) return;

        if (target == null || !target.isAlive()) {
            target = mage.getTarget();
            if (target == null) return;
        }
        mage.setTarget(target);

        double dist = mage.distanceTo(target);
        double castRange    = spell.RANGE * 1.0; // cast if within full range
        double approachRange = spell.RANGE * 0.7; // navigation target

        double txWarmup = target.getX() - mage.getX();
        double tzWarmup = target.getZ() - mage.getZ();
        float yawToTarget = (float)(Math.toDegrees(Math.atan2(-txWarmup, tzWarmup)));

        // Cooldown strafe
        if (hasCast) {
            if (mage.tickCount < nextCastTick) {
                mage.setYHeadRot(yawToTarget);
                mage.setYBodyRot(yawToTarget);
                strafeTimer--;
                if (strafeTimer <= 0) pickNewStrafe(dist, approachRange);
                applyStrafe();
                return;
            }
            hasCast = false;
            castTimer = 0;
            mage.setCastTarget(null);
        }

        if (dist > approachRange) {
            // Use pathfinding navigation so walls/holes are handled
            castTimer = 0;
            mage.setCastTarget(null);
            mage.setYHeadRot(yawToTarget);
            mage.setYBodyRot(yawToTarget);
            mage.getNavigation().moveTo(target, speedModifier * 1.6); // aggressive chase
            return;
        }

        // Within approach range — stop nav and cast if also within cast range
        mage.getNavigation().stop();

        if (dist > castRange) {
            // Between approachRange and castRange shouldn't happen with 0.8/1.0
            // but guard anyway — strafe in place
            strafeTimer--;
            if (strafeTimer <= 0) pickNewStrafe(dist, approachRange);
            applyStrafe();
            return;
        }

        // In cast range
        castTimer++;
        mage.setYHeadRot(yawToTarget);
        mage.setYBodyRot(yawToTarget);

        if (castTimer <= WARMUP_TICKS) {
            strafeTimer--;
            if (strafeTimer <= 0) pickNewStrafe(dist, approachRange);
            applyStrafe();

        } else {
            if (mage.getCastTarget() == null) {
                mage.setCastTarget(new Vec3(target.getX(), target.getY(), target.getZ()));
            }

            Vec3 locked = mage.getCastTarget();
            double dx = locked.x - mage.getX();
            double dz = locked.z - mage.getZ();
            float yawLocked = (float)(Math.toDegrees(Math.atan2(-dx, dz)));
            mage.setYHeadRot(yawLocked);
            mage.setYBodyRot(yawLocked);

            if (castTimer == TOTAL_CAST) {
                spell.cast(mage.level(), mage, mage.getItemBySlot(EquipmentSlot.MAINHAND));
                hasCast = true;
                nextCastTick = mage.tickCount + COOLDOWN_TICKS;
                pickNewStrafe(dist, approachRange);
            }
        }
    }
}