package bunger.group.alex.entity.goblin;

import bunger.group.alex.entity.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class GoblinChiefEntity extends Monster implements GoblinFaction, GoblinPatrolMember {

    private static final int GRUNT_MIN  = 4;
    private static final int GRUNT_MAX  = 10;
    private static final int RANGER_MIN = 2;
    private static final int RANGER_MAX = 6;
    private static final int MAGE_MIN   = 1;
    private static final int MAGE_MAX   = 2;

    @Nullable private GoblinChiefEntity patrol = null;
    @Nullable private UUID patrolUUID = null;
    private boolean enraged = false;
    private int tickCounter = 0;
    private boolean hasAlerted = false;



    public GoblinChiefEntity(EntityType<? extends GoblinChiefEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 100;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 100.0)
                .add(Attributes.ARMOR, 10.0)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));

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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_AXE));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.DIAMOND_AXE));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);

        if (!(groupData instanceof GoblinGroupSpawnData) && level instanceof ServerLevel serverLevel) {
            spawnParty(serverLevel, difficulty);
        }

        return groupData;
    }

    @Override
    public void tick() {
        super.tick();
        tickCounter++;
        if (enraged && !this.level().isClientSide() && (tickCounter % 4 == 0)) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.ANGRY_VILLAGER,
                    this.getX() + (this.random.nextDouble() - 0.5) * 0.8,
                    this.getY() + this.getBbHeight() + 0.2,
                    this.getZ() + (this.random.nextDouble() - 0.5) * 0.8,
                    1, 0, 0, 0, 0.01
            );
        }
        // Share aggro with patrol members every 10 ticks
        if (!this.level().isClientSide() && tickCounter % 10 == 0 && this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.getEntitiesOfClass(
                    Monster.class,
                    this.getBoundingBox().inflate(100.0),
                    e -> e instanceof GoblinPatrolMember m && m.getPatrol() == this
            ).forEach(e -> e.setTarget(target));
        }
    }

    private void spawnParty(ServerLevel level, DifficultyInstance difficulty) {
        var random = level.getRandom();

        int grunts  = GRUNT_MIN  + random.nextInt(GRUNT_MAX  - GRUNT_MIN  + 1);
        int rangers = RANGER_MIN + random.nextInt(RANGER_MAX - RANGER_MIN + 1);
        int mages   = MAGE_MIN   + random.nextInt(MAGE_MAX   - MAGE_MIN   + 1);

        for (int i = 0; i < grunts;  i++) spawnMember(level, difficulty, ModEntityTypes.GOBLIN_GRUNT);
        for (int i = 0; i < rangers; i++) spawnMember(level, difficulty, ModEntityTypes.GOBLIN_RANGER);
        for (int i = 0; i < mages;   i++) spawnMember(level, difficulty, ModEntityTypes.GOBLIN_MAGE);
    }

    private <T extends Monster & GoblinFaction & GoblinPatrolMember> void spawnMember(
            ServerLevel level, DifficultyInstance difficulty, EntityType<T> type) {

        T member = type.create(level, EntitySpawnReason.MOB_SUMMONED);
        if (member == null) return;

        // Explicitly teleport to chief's position before finalizeSpawn
        member.setPos(this.getX(), this.getY(), this.getZ());
        member.finalizeSpawn(level, difficulty, EntitySpawnReason.MOB_SUMMONED, new GoblinGroupSpawnData());
        member.setPatrol(this);
        level.addFreshEntity(member);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return distanceToClosestPlayer > 200 * 200;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (patrolUUID != null) {
            output.putLong("PatrolUUIDMost", patrolUUID.getMostSignificantBits());
            output.putLong("PatrolUUIDLeast", patrolUUID.getLeastSignificantBits());
        }
        output.putBoolean("HasAlerted", hasAlerted);
        output.putBoolean("Enraged", enraged);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        long most  = input.getLongOr("PatrolUUIDMost", 0L);
        long least = input.getLongOr("PatrolUUIDLeast", 0L);
        if (most != 0L || least != 0L) patrolUUID = new UUID(most, least);
        hasAlerted   = input.getBooleanOr("HasAlerted", false);
        enraged      = input.getBooleanOr("Enraged", false);
        if (enraged) {
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.32);
            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8.0);
        }
    }

    @Override
    public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effect) {
        if (effect.getEffect().is(MobEffects.POISON)) return false;
        return super.canBeAffected(effect);
    }

    @Override
    public boolean hurtServer(net.minecraft.server.level.ServerLevel level,
                              net.minecraft.world.damagesource.DamageSource source, float amount) {
        var direct = source.getDirectEntity();
        if (direct instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow arrow) {
            if (arrow.getOwner() instanceof GoblinFaction) return false;
        }

        boolean result = super.hurtServer(level, source, amount);

        if (result && !enraged && this.getHealth() < this.getMaxHealth() * 0.5f) {
            Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.32);
            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(8.0);
            this.level().broadcastEntityEvent(this, (byte) 4);
            this.enraged = true;
        }

        return result;
    }

    @Override
    public void setTarget(@Nullable net.minecraft.world.entity.LivingEntity target) {
        super.setTarget(target);
        if (target instanceof Player && !hasAlerted && !this.level().isClientSide()) {
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2).value(),
                    this.getSoundSource(),
                    16.0f, 1.0f
            );
            hasAlerted = true;
        }
    }
}