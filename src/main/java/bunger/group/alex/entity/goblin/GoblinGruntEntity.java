package bunger.group.alex.entity.goblin;

import bunger.group.alex.entity.goal.GoblinPatrolGoal;
import net.minecraft.world.DifficultyInstance;
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

import java.util.UUID;

public class GoblinGruntEntity extends Monster implements GoblinFaction, GoblinPatrolMember {

    public static final float SPEAR_CHANCE = 0.33f;
    public static final float AXE_CHANCE   = 0.33f;
    public static final float STONE_CHANCE = 0.50f;

    @Nullable private GoblinChiefEntity patrol = null;
    @Nullable private UUID patrolUUID = null;

    public GoblinGruntEntity(EntityType<? extends GoblinGruntEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 10;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 25.0)
                .add(Attributes.ARMOR, 6.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, false));
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

    private ItemStack pickTieredWeapon(ItemStack stone, ItemStack iron) {
        return this.random.nextFloat() < STONE_CHANCE ? stone : iron;
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, DifficultyInstance difficulty) {
        float weaponRoll = random.nextFloat();
        if (weaponRoll < SPEAR_CHANCE) {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_SPEAR), new ItemStack(Items.IRON_SPEAR)));
        } else if (weaponRoll < SPEAR_CHANCE + AXE_CHANCE) {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_AXE), new ItemStack(Items.IRON_AXE)));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_SWORD), new ItemStack(Items.IRON_SWORD)));
        }
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
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return patrol == null || !patrol.isAlive();
    }
}