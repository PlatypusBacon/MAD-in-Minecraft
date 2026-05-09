package bunger.group.tyler3.entity;

import bunger.group.tyler3.sounds.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DudeEntity extends PathfinderMob implements Enemy {

    private static final double BASE_FOLLOW_RANGE  = 6.0;
    private static final double AGGRO_FOLLOW_RANGE = 256.0;
    private int attackCooldown = 0;

    public DudeEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.FOLLOW_RANGE, 6.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }
    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        var followRange = this.getAttribute(Attributes.FOLLOW_RANGE);
        if (followRange != null) {
            followRange.setBaseValue(target != null ? AGGRO_FOLLOW_RANGE : BASE_FOLLOW_RANGE);
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation nav = new GroundPathNavigation(this, level);
        nav.setCanFloat(true);
        nav.getNodeEvaluator().setCanPassDoors(true);
        nav.getNodeEvaluator().setCanOpenDoors(true);
        return nav;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DudeGoal(this));       // custom — only runs when target != null
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.3)); // was 0.8
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean checkSpawnRules(EntityType<DudeEntity> type, ServerLevelAccessor level,
                                          EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        //return pos.getY() < 50
        //        && level.getRawBrightness(pos, 0) == 0
        //        && DudeEntity.checkMobSpawnRules(type, level, reason, pos, random);
        return Mob.checkMobSpawnRules(type, level, reason, pos, random);
    }
    @Override
    public float maxUpStep() {
        return 1.0F;
    }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        setIdleEquipment();
        this.setCustomName(Component.literal("PlatypusBacon"));
        this.setCustomNameVisible(true);
        return data;
    }
    public void setIdleEquipment() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
    }

    public void setCombatEquipment() {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.SHIELD));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
    }

    public boolean isInCombat() {
        return this.getTarget() != null;
    }

    @Override
    public void tick() {
        super.tick();

        if (attackCooldown > 0) attackCooldown--;

        if (isInCombat()) {
            if (!this.isSprinting()) this.setSprinting(true);
            // Equipment swap is handled in the goal's start()
        } else {
            if (this.isSprinting()) this.setSprinting(false);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 280;
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && this.isSprinting() && target instanceof LivingEntity living) {
            living.knockback(
                    0.4F,
                    Math.sin(this.getYRot() * Math.PI / 180F),
                    -Math.cos(this.getYRot() * Math.PI / 180F)
            );
        }
        return hit;
    }

    @Override
    protected SoundEvent getAmbientSound() { return ModSounds.DUDE_IDLE; }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ZOMBIE_HURT; }
    @Override
    protected SoundEvent getDeathSound() { return SoundEvents.ZOMBIE_DEATH; }
}