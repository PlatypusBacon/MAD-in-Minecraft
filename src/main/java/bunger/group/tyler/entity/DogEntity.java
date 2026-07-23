package bunger.group.tyler.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor; // <-- add this
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class DogEntity extends Mob {

    private static final int ROAR_TICKS = 200;
    private static final int SPAWN_DESTROY_RADIUS = 3;
    private static final float HEALTH_THRESHOLD = 980.0F;
    private ServerPlayer player = null;

    public DogEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    public static void applyDarknessAround(final ServerLevel level, final Vec3 position, @Nullable final Entity source, final int darknessRadius) {
        MobEffectInstance darkness = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
        MobEffectUtil.addEffectToPlayersAround(level, source, position, darknessRadius, darkness, 200);
    }
    public void setTrackedPlayer(ServerPlayer player) {
        this.player = player;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            final ServerLevelAccessor level,
            final DifficultyInstance difficulty,
            final EntitySpawnReason spawnReason,
            @Nullable final SpawnGroupData groupData
    ) {
        Player nearest = player != null && player.isAlive()
                ? player
                : level().getNearestPlayer(this, 64);
        double fdx = nearest.getX() - this.getX();
        double fdz = nearest.getZ() - this.getZ();
        float yaw = (float)(Math.toDegrees(Math.atan2(-fdx, fdz)));
        this.setYRot(yaw);
        this.yRotO = yaw;
        this.setYHeadRot(yaw);
        if (level instanceof ServerLevel serverLevel) {
            destroyBlocksAround(serverLevel, this.blockPosition(), SPAWN_DESTROY_RADIUS);
        }
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    private static void destroyBlocksAround(final ServerLevel level, final BlockPos center, final int radius) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int radiusSqr = radius * radius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z > radiusSqr) continue;
                    mutable.setWithOffset(center, x, y, z);
                    BlockState state = level.getBlockState(mutable);
                    if (!state.isAir() && state.getDestroySpeed(level, mutable) >= 0.0F) {
                        level.destroyBlock(mutable, false);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level() instanceof ServerLevel serverLevel && this.isAlive()) {
            if (this.tickCount % ROAR_TICKS == 0) {
                roar(serverLevel);
            }

            tickSonicBoomCharge(serverLevel);

            if (this.sonicBoomChargeTicks < 0 && this.tickCount % SONIC_BOOM_COOLDOWN_TICKS == 0) {
                trySonicBoom(serverLevel);
            }

            if (this.getHealth() <= HEALTH_THRESHOLD) {
                fuckOff(serverLevel);
            }
        }
    }

    private void roar(final ServerLevel serverLevel) {
        this.playSound(SoundEvents.WARDEN_ROAR, 10.0F, this.getVoicePitch());
        applyDarknessAround(serverLevel, this.position(), this, 20);
    }

    private void fuckOff(final ServerLevel serverLevel) {
        serverLevel.sendParticles(
                ParticleTypes.PORTAL,
                this.getX(), this.getY() + this.getBbHeight() / 2.0, this.getZ(),
                80, 0.5, 0.5, 0.5, 0.15
        );
        this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        this.discard();
    }
    //Stolen from warden
    private static final int SONIC_BOOM_ATTACK_RANGE_XZ = 15;
    private static final int SONIC_BOOM_ATTACK_RANGE_Y = 20;
    private static final int SONIC_BOOM_CHARGE_TICKS = 34;
    private static final int SONIC_BOOM_COOLDOWN_TICKS = 60;

    private int sonicBoomChargeTicks = -1; // -1 = not charging

    private void trySonicBoom(final ServerLevel serverLevel) {
        if (this.sonicBoomChargeTicks >= 0) {
            return;
        }

        LivingEntity target = findSonicBoomTarget(serverLevel);
        if (target == null) {
            return;
        }

        // start the charge — telegraph like vanilla's SonicBoom#start
        this.sonicBoomChargeTicks = 0;
        serverLevel.broadcastEntityEvent(this, (byte) 62);
        this.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
    }

    private void tickSonicBoomCharge(final ServerLevel serverLevel) {
        if (this.sonicBoomChargeTicks < 0) {
            return;
        }

        this.sonicBoomChargeTicks++;

        if (this.sonicBoomChargeTicks >= SONIC_BOOM_CHARGE_TICKS) {
            fireSonicBoom(serverLevel);
            this.sonicBoomChargeTicks = -1; // reset, cooldown handled by outer tick interval
        }
    }

    @Nullable
    private LivingEntity findSonicBoomTarget(final ServerLevel serverLevel) {
        return serverLevel.getNearestEntity(
                LivingEntity.class,
                TargetingConditions.forCombat().range(SONIC_BOOM_ATTACK_RANGE_XZ),
                this,
                this.getX(), this.getY(), this.getZ(),
                this.getBoundingBox().inflate(SONIC_BOOM_ATTACK_RANGE_XZ, SONIC_BOOM_ATTACK_RANGE_Y, SONIC_BOOM_ATTACK_RANGE_XZ)
        );
    }

    private void fireSonicBoom(final ServerLevel serverLevel) {
        LivingEntity target = findSonicBoomTarget(serverLevel);
        if (target == null || !target.isAlive()) {
            return; // target moved away or died during the charge
        }

        Vec3 source = this.position().add(0, this.getBbHeight() / 2.0, 0); // no WARDEN_CHEST attachment on this entity
        Vec3 delta = target.getEyePosition().subtract(source);
        Vec3 normalize = delta.normalize();
        int steps = (int) (Math.floor(delta.length()) + 7);

        for (int i = 1; i < steps; i++) {
            Vec3 particlePos = source.add(normalize.scale(i));
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
        }

        this.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);

        if (target.hurtServer(serverLevel, serverLevel.damageSources().sonicBoom(this), 10.0F)) {
            double knockbackVertical = 0.5 * (1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            double knockbackHorizontal = 2.5 * (1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            target.push(normalize.x() * knockbackHorizontal, normalize.y() * knockbackVertical, normalize.z() * knockbackHorizontal);
        }
    }
}