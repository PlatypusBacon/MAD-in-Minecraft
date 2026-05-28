package bunger.group.alex.entity.goblin;

import bunger.group.alex.entity.goal.GoblinPatrolGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GoblinRangerEntity extends Monster implements RangedAttackMob, GoblinFaction, GoblinPatrolMember {

    private static final int ATTACK_INTERVAL_NORMAL = 40;

    @Nullable private GoblinChiefEntity patrol = null;
    @Nullable private UUID patrolUUID = null;

    public GoblinRangerEntity(EntityType<? extends GoblinRangerEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 12;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 25.0)
                .add(Attributes.ARMOR, 2.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedBowAttackGoal<>(this, 1.1, ATTACK_INTERVAL_NORMAL, 15.0F));
        this.goalSelector.addGoal(2, new GoblinPatrolGoal<>(this, 1.0));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void setPatrol(@Nullable GoblinChiefEntity patrol) {
        this.patrol = patrol;
        this.patrolUUID = patrol != null ? patrol.getUUID() : null;
    }

    @Override
    @Nullable
    public GoblinChiefEntity getPatrol() {
        return patrol;
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return groupData;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        ItemStack bowItem = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        ItemStack projectile = this.getProjectile(bowItem);
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, projectile, power, bowItem);

        arrow.setOwner(this);
        arrow.setPos(this.getX(), this.getEyeY() - 0.1D, this.getZ());

        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);
        double dy = target.getY(0.35D) - arrow.getY();
        double arc = horizontalDist * 0.13D;

        arrow.shoot(dx, dy + arc, dz, 1.8F, 8.0F);
        this.level().addFreshEntity(arrow);

        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F,
                1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (patrolUUID != null) {
            output.putLong("PatrolUUIDMost", patrolUUID.getMostSignificantBits());
            output.putLong("PatrolUUIDLeast", patrolUUID.getLeastSignificantBits());
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        long most  = input.getLongOr("PatrolUUIDMost", 0L);
        long least = input.getLongOr("PatrolUUIDLeast", 0L);
        if (most != 0L || least != 0L) patrolUUID = new UUID(most, least);
    }

    @Override
    public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effect) {
        if (effect.getEffect().is(net.minecraft.world.effect.MobEffects.POISON)) return false;
        return super.canBeAffected(effect);
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel level,
                              net.minecraft.world.damagesource.DamageSource source, float amount) {
        var direct = source.getDirectEntity();
        if (direct instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow arrow) {
            if (arrow.getOwner() instanceof GoblinFaction) return false;
        }
        if (direct instanceof GoblinFaction) return false;
        if (source.getEntity() instanceof GoblinFaction) return false;
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return patrol == null || !patrol.isAlive();
    }

    public static boolean canSpawn(EntityType<GoblinRangerEntity> type,
                                   ServerLevelAccessor level,
                                   EntitySpawnReason spawnReason,
                                   BlockPos pos,
                                   RandomSource random) {

        return Monster.isDarkEnoughToSpawn(level, pos, random)
                && Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random);
    }
}