package bunger.group.alex.entity.goal;

import bunger.group.alex.entity.MageMob;
import bunger.group.alex.item.SpellTemplate;
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
    private LivingEntity target;
    private int castTimer;
    private int nextCastTick;

    public MediumCastGoal(MageMob mage) {
        this.mage = mage;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = mage.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (mage.tickCount < nextCastTick) return false;
        return mage.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof SpellTemplate;
    }

    @Override
    public boolean canContinueToUse() {
        return castTimer < TOTAL_CAST && target != null && target.isAlive();
    }

    @Override
    public void start() {
        castTimer = 0;
        mage.setCastTarget(null);
        mage.getNavigation().stop();
    }

    @Override
    public void stop() {
        nextCastTick = mage.tickCount + COOLDOWN_TICKS;
        mage.setCastTarget(null);
        castTimer = 0;
    }

    @Override
    public void tick() {
        castTimer++;

        if (castTimer <= WARMUP_TICKS) {
            mage.getLookControl().setLookAt(
                    target.getX(), target.getY(), target.getZ(),
                    (float) mage.getMaxHeadYRot(), (float) mage.getMaxHeadXRot());

        } else {
            if (mage.getCastTarget() == null) {
                mage.setCastTarget(new Vec3(target.getX(), target.getY(), target.getZ()));
            }

            Vec3 locked = mage.getCastTarget();
            mage.getLookControl().setLookAt(
                    locked.x, locked.y, locked.z,
                    (float) mage.getMaxHeadYRot(), (float) mage.getMaxHeadXRot());

            if (castTimer == TOTAL_CAST) {
                ItemStack held = mage.getItemBySlot(EquipmentSlot.MAINHAND);
                if (held.getItem() instanceof SpellTemplate spell) {
                    spell.cast(mage.level(), mage, held);
                }
            }
        }
    }
}