package bunger.group.alex.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;


public class WraithEntity extends Monster {

    public WraithEntity(Level world) {
        this(ModEntityTypes.WRAITH, world);
    }

    public WraithEntity(EntityType<? extends WraithEntity> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FLYING_SPEED, 1.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, world);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    // copied from mob
    private boolean isSunBurnTick() {
        if (!this.level().isClientSide() && (Boolean)this.level().environmentAttributes().getValue(EnvironmentAttributes.MONSTERS_BURN, this.position())) {
            float br = this.getLightLevelDependentMagicValue();
            BlockPos roundedPos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            boolean isInNonBurnableBlock = this.isInWaterOrRain() || this.isInPowderSnow || this.wasInPowderSnow;
            if (br > 0.5F && this.random.nextFloat() * 30.0F < (br - 0.4F) * 2.0F && !isInNonBurnableBlock && this.level().canSeeSky(roundedPos)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);

        if (!this.level().isClientSide() && this.isSunBurnTick()) {
            this.igniteForSeconds(8);
        }

        // limit to 8 blocks above ground
        BlockPos below = this.blockPosition();
        int groundY = below.getY();
        // scan downward to find ground
        for (int i = 0; i < 16; i++) {
            BlockPos check = BlockPos.containing(this.getX(), this.getY() - i, this.getZ());
            if (this.level().getBlockState(check).isSolid()) {
                groundY = check.getY();
                break;
            }
        }

        if (this.getY() > groundY + 8) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x,
                    Math.min(this.getDeltaMovement().y, -0.1),
                    this.getDeltaMovement().z
            );
        } else if (this.getY() < groundY + 2) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x,
                    Math.max(this.getDeltaMovement().y, 0.02),
                    this.getDeltaMovement().z
            );
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        // immune to projectiles
        if (source.is(DamageTypeTags.IS_PROJECTILE)) return false;
        // half damage from players
        if (source.getEntity() instanceof Player) {
            amount *= 0.5f;
        }
        if (source.is(DamageTypeTags.IS_FALL)) return false; // No Fall Damage

        return super.hurtServer(level, source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(strength * 0.5, x, z);
    }

}