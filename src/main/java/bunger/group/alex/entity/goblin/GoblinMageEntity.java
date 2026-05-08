package bunger.group.alex.entity.goblin;

import bunger.group.alex.entity.MageMob;
import bunger.group.alex.entity.goal.GoblinPatrolGoal;
import bunger.group.alex.entity.goal.MediumCastGoal;
import bunger.group.alex.item.ModItems;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GoblinMageEntity extends MageMob implements GoblinFaction, GoblinPatrolMember {

    public static final float SPELL_A_CHANCE = 0.50f;

    @Nullable private GoblinChiefEntity patrol = null;
    @Nullable private UUID patrolUUID = null;

    public GoblinMageEntity(EntityType<? extends GoblinMageEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 25.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MediumCastGoal(this));
        this.goalSelector.addGoal(2, new GoblinPatrolGoal<>(this, 1.0));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0f));

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
        ItemStack spell = random.nextFloat() < SPELL_A_CHANCE
                ? new ItemStack(ModItems.IMPALE)
                : new ItemStack(ModItems.LIGHTNING);
        this.setItemSlot(EquipmentSlot.MAINHAND, spell);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
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
        return super.hurtServer(level, source, amount);
    }
    @Override
    protected void dropCustomDeathLoot(net.minecraft.server.level.ServerLevel level,
                                       net.minecraft.world.damagesource.DamageSource source, boolean recentlyHit) {
        // no drops
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return patrol == null || !patrol.isAlive();
    }

}