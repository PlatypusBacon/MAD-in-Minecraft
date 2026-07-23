package bunger.group.alex.entity;

import bunger.group.alex.entity.goal.LongbowAttackGoal;
import bunger.group.alex.item.Longbow;
import bunger.group.alex.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SkeletonRangerEntity extends Skeleton {

    private LongbowAttackGoal<SkeletonRangerEntity> bowGoal;

    public SkeletonRangerEntity(final EntityType<? extends SkeletonRangerEntity> type, final Level level) {
        super(type, level);
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.ARMOR, 4.0)
                .add(Attributes.FOLLOW_RANGE, 38.0);
    }

    @Override
    protected void registerGoals() {
        this.bowGoal = new LongbowAttackGoal<>(this, 1.0, 20, 30.0F);

        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(4, this.bowGoal);
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 38.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.LONGBOW));
    }

    @Override
    public boolean canUseNonMeleeWeapon(ItemStack item) {
        return item.getItem() == ModItems.LONGBOW;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        ItemStack bowItem = this.getMainHandItem();
        ItemStack projectile = this.getProjectile(bowItem);
        AbstractArrow arrow = this.getArrow(projectile, power, bowItem);
        Longbow longbow = (Longbow) bowItem.getItem();


        longbow.shootProjectile(this, arrow, 0, Longbow.POWER_MULT,
                (float)(6 - serverLevel.getDifficulty().getId() * 2), 0.0F, target);

        serverLevel.addFreshEntity(arrow);

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        var equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && equippable.slot() == EquipmentSlot.CHEST) {
            return false;
        }
        return super.wantsToPickUp(level, itemStack);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean isFreezeConverting() {
        return false;
    }

    @Override
    public void setFreezeConverting(boolean isConverting) {
        // do nothing
    }

    public static boolean canSpawn(EntityType<SkeletonRangerEntity> type,
                                   ServerLevelAccessor level,
                                   EntitySpawnReason spawnReason,
                                   BlockPos pos,
                                   RandomSource random) {

        return Monster.isDarkEnoughToSpawn(level, pos, random)
                && Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random);
    }

}