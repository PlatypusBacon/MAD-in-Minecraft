package bunger.group.alex.wizardry.mobs;

import bunger.group.alex.wizardry.items.spells.ScrollItem;
import bunger.group.alex.wizardry.items.spells.LightningSpells;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Wizard extends PathfinderMob {

    private int castCooldown = 0;

    public Wizard(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(LightningSpells.LIGHTNING));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 30.0);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (level.isClientSide) return;

        if (castCooldown > 0) castCooldown--;

        LivingEntity target = this.getTarget();

        if (target != null) {
            this.lookAt(target, 30F, 30F);

            if (castCooldown == 0) {
                castSpell();
                castCooldown = 60;
            }
        }
    }

    private void castSpell() {
        ItemStack stack = this.getMainHandItem();

        if (stack.getItem() instanceof ScrollItem scroll) {
            scroll.cast(this.level, this, stack);
            this.swing(InteractionHand.MAIN_HAND);
        }
    }
}