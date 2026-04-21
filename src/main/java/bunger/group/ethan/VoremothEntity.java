package bunger.group.ethan;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.Ghast.GhastMoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffects;
import org.jspecify.annotations.Nullable;


// TODO: add mob attacks
// TODO: fix pathfinding


public class VoremothEntity extends PathfinderMob {

    private int laserTimer = 0;
    private int laserCooldown = 0;
    private static final int LASER_WARMUP = 60;
    @Nullable
    public LivingEntity laserTarget = null;
    public int getLaserTimer() { return laserTimer; }


    public VoremothEntity(EntityType<? extends VoremothEntity> entityType, Level world) {
        super(entityType, world);
        //this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 3.0)
                .add(Attributes.SCALE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0F)
                .add(Attributes.CAMERA_DISTANCE, 1.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 100.0F, 1.0F));
        this.moveControl = new FlyingMoveControl(this, 0, true);
        this.setNoGravity(true);
        // this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        // this.goalSelector.addGoal(5, new GhastMoveControl(this));
        //this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 0.1D));

    }

    public static final SoundEvent VOREMOTH_AMBIENT = new SoundEvent(
        Identifier.fromNamespaceAndPath("mutually-assured-destruction", "voremoth_ambient"),
        Optional.of(50.0F)
    );

    private static final SoundEvent[] AMBIENT_SOUNDS = {
        MutuallyAssuredDestruction.VOREMOTH1,
        MutuallyAssuredDestruction.VOREMOTH2,
        MutuallyAssuredDestruction.VOREMOTH3,
        MutuallyAssuredDestruction.VOREMOTH4,
        MutuallyAssuredDestruction.VOREMOTH5
    };


    public final ServerBossEvent bossBar = new ServerBossEvent(
        Mth.createInsecureUUID(level().getRandom()),
        Component.literal("Voremoth, Excommunicado Imortalis"),
        BossEvent.BossBarColor.RED,
        BossEvent.BossBarOverlay.PROGRESS
    );

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

@Override
public void customServerAiStep(ServerLevel level) {
    super.customServerAiStep(level);


    // Update health bar
    bossBar.setProgress(this.getHealth() / this.getMaxHealth());

    if (laserCooldown > 0) {
        laserCooldown--;
        this.setLaserTarget(null);
        return;
    }

    Player target = level.getNearestPlayer(this, 50.0);
    if (target != null) {
        this.setLaserTarget(target);

        if (laserTimer == 0) {
            this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                MutuallyAssuredDestruction.VOREMOTH_CHARGE,
                SoundSource.AMBIENT,
                0.5F,
                0.9F
            );
        }
        laserTimer++;

        if (laserTimer >= LASER_WARMUP) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(
                level, EntitySpawnReason.TRIGGERED
            );
            if (lightning != null) {
                lightning.setPos(target.getX(), target.getY(), target.getZ());
                lightning.setVisualOnly(false);
                level.addFreshEntity(lightning);
            }
            laserTimer = 0;
            laserCooldown = 100;
            this.setLaserTarget(null);
        }
    } else {
        laserTimer = 0;
        this.setLaserTarget(null);
    }
}

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }


    @Override
    public void playAmbientSound() {
        SoundEvent sound = AMBIENT_SOUNDS[this.random.nextInt(AMBIENT_SOUNDS.length)];
        float pitch = 0.6F;
        float volume = 0.9F;

        this.level().playSound(
            null,
            this.getX(), this.getY(), this.getZ(),
            sound,
            SoundSource.AMBIENT,
            volume,
            pitch
        );
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100 + this.random.nextInt(160); 
    }

    @Override
    public void playHurtSound(final DamageSource source) {
        this.level().playSound(
            null,
            //new BlockPos(((int) Math.round(source.getSourcePosition().x)), (int) Math.round(source.getSourcePosition().y), (int) Math.round(source.getSourcePosition().z)),
            this.getX(), this.getY(), this.getZ(),
            MutuallyAssuredDestruction.VOREMOTH_HIT,
            SoundSource.AMBIENT,
            0.75F,
            1.0F
        );
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    // @Override
    // public void onRemovedFromLevel() {
    //     super.onRemovedFromLevel();
    //     System.out.println("VOREMOTHTEST: Voremoth removed from level, reason: " + this.getRemovalReason());
    // }





    private int ambientTimer = 0;
    @Override
    public void baseTick() {
        super.baseTick();
        ambientTimer--;
        if (ambientTimer <= 0) {
            SoundEvent sound = VOREMOTH_AMBIENT;
            float pitch = 1.0F;
            float volume = 0.1F;

            this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                sound,
                SoundSource.AMBIENT,
                volume,
                pitch
        );
            ambientTimer = 2880;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        // only allow damage from your custom source
        if (source.is(ModDamageTypes.BEACON_DAMAGE)) {
            return super.hurtServer(level, level.damageSources().source(ModDamageTypes.BEACON_DAMAGE), amount);
           
        }
        System.out.println("VOREMOTHDAMAGE: Blocked damage from source " + source + " with amount " + amount);
        return false; // block everything else
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        System.out.println("VOREMOTHTEST: setInvulnerable called with: " + invulnerable);
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println("  " + element);
        }
        super.setInvulnerable(false);
    }


    private static final EntityDataAccessor<Integer> LASER_TARGET_ID = SynchedEntityData.defineId(VoremothEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LASER_TARGET_ID, -1);
    }

    public void setLaserTarget(@Nullable LivingEntity target) {
        this.laserTarget = target;
        this.entityData.set(LASER_TARGET_ID, target != null ? target.getId() : -1);
    }

    public int getLaserTargetId() {
        return this.entityData.get(LASER_TARGET_ID);
    }
    

   

    public static void initialize() {

    }

}