package bunger.group.ethan;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast.GhastMoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffects;


public class VoremothEntity extends PathfinderMob {

    public VoremothEntity(EntityType<? extends VoremothEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 1.0)
                .add(Attributes.SCALE, 5.0);
    }

    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 50.0F, 1.0F));
        this.moveControl = new FlyingMoveControl(this, 2, true);
        this.setNoGravity(true);
        // this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        // this.goalSelector.addGoal(5, new GhastMoveControl(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.0D));

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


    private final ServerBossEvent bossBar = new ServerBossEvent(
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
        bossBar.setProgress(this.getHealth() / this.getMaxHealth());
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
        float pitch = 0.5F;
        float volume = 0.7F;

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
        return 160 + this.random.nextInt(160); 
    }


    private int ambientTimer = 0;
    @Override
    public void baseTick() {
        super.baseTick();
        ambientTimer--;
        if (ambientTimer <= 0) {
            SoundEvent sound = VOREMOTH_AMBIENT;
            float pitch = 1.0F;
            float volume = 0.2F;

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
        if (this.level().isClientSide()) {
            // spawn particles around the entity every tick
            for (int i = 0; i < 150; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 60;
                double offsetY = (this.random.nextDouble() - 0.5) * 60;
                double offsetZ = (this.random.nextDouble() - 0.5) * 60;
                this.level().addParticle(
                    ParticleTypes.CRIMSON_SPORE,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    0, 0, 0
                );

            }
        }
    }



    // public static final SoundEvent HEARTBEAT = new SoundEvent(
    //     Identifier.fromNamespaceAndPath("mutually-assured-destruction", "heartbeat"),
    //     Optional.of(50.0F)
    // );
    // public static final SoundEvent DRIPPING = new SoundEvent(
    //     Identifier.fromNamespaceAndPath("mutually-assured-destruction", "dripping"),
    //     Optional.of(25.0F)
    // );



    // private int heartbeatTimer = 0;
    // private int drippingTimer = 0;

    // @Override
    // public void baseTick() {
    //     super.baseTick();
    //     heartbeatTimer--;
    //     drippingTimer--;
    //     if (heartbeatTimer <= 0) {
    //         if (!this.level().isClientSide()) {
    //             double maxRange = 50.0;
    //             for (Player player : this.level().players()) {
    //                 double distance = this.distanceTo(player);
    //                 if (distance <= maxRange) {
    //                     float volume = (float)(1.0 - (distance / maxRange));
    //                     volume = Math.max(0.0F, Math.min(1.0F, volume));
    //                     ClientboundSoundPacket packet = new ClientboundSoundPacket(
    //                         BuiltInRegistries.SOUND_EVENT.wrapAsHolder(VoremothEntity.HEARTBEAT),
    //                         SoundSource.HOSTILE,
    //                         this.getX(), this.getY(), this.getZ(),
    //                         volume,
    //                         1.0F,
    //                         this.level().getRandom().nextLong()
    //                     );
    //                     ((ServerPlayer) player).connection.send(packet);
    //                 } else if(distance <= maxRange * 2) {
    //                     ClientboundSoundPacket packet = new ClientboundSoundPacket(
    //                         BuiltInRegistries.SOUND_EVENT.wrapAsHolder(VoremothEntity.HEARTBEAT),
    //                         SoundSource.HOSTILE,
    //                         this.getX(), this.getY(), this.getZ(),
    //                         0.1F,
    //                         1.0F,
    //                         this.level().getRandom().nextLong()
    //                     );
    //                     ((ServerPlayer) player).connection.send(packet);

    //                 }
                    
    //             }
    //         }
    //         heartbeatTimer = 39;
    //     }

    //     if (drippingTimer <= 0) {
    //         if (!this.level().isClientSide()) {
    //             double maxRange = 40.0;
    //             for (Player player : this.level().players()) {
    //                 double distance = this.distanceTo(player);
    //                 if (distance <= maxRange) {
    //                     float volume = (float)(1.0 - (distance / maxRange));
    //                     volume = Math.max(0.5F, Math.min(1.0F, volume));
    //                     ClientboundSoundPacket packet = new ClientboundSoundPacket(
    //                         BuiltInRegistries.SOUND_EVENT.wrapAsHolder(VoremothEntity.DRIPPING),
    //                         SoundSource.HOSTILE,
    //                         this.getX(), this.getY(), this.getZ(),
    //                         volume,
    //                         1.0F,
    //                         this.level().getRandom().nextLong()
    //                     );
    //                     ((ServerPlayer) player).connection.send(packet);
    //                 }
                    
    //             }
    //         }
    //         drippingTimer = 259;
    //     }
    // }

    // @Override
    // public void die(DamageSource source) {
    //     super.die(source);
    //     if (source.getEntity() instanceof Player player) {
    //         player.addEffect(new MobEffectInstance(
    //             BuiltInRegistries.MOB_EFFECT.wrapAsHolder(RedDarknessEffect.RED_DARKNESS),
    //             //MobEffects.DARKNESS,
    //             1000,
    //             0
    //         ));
    //         MutuallyAssuredDestruction.RED_RAIN_PLAYERS.put(player.getUUID(), player.level().getGameTime() + 1200);
    //     }
    // }

    // @Override
    // protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
    //     super.dropCustomDeathLoot(level, source, recentlyHit);
    //     ItemEntity drop = new ItemEntity(
    //         level,
    //         this.getX(), this.getY(), this.getZ(),
    //         new ItemStack(MutuallyAssuredDestruction.ALTAR_FRAGMENT)
    //     );
    //     level.addFreshEntity(drop);
    // }

    public static void initialize() {

    }

}