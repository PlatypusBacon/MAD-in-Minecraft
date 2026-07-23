package bunger.group.ethan;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.network.chat.Component;



public class ProphetEntity extends PathfinderMob {

    public ProphetEntity(EntityType<? extends ProphetEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 50.0F, 1.0F));
    }

    public static final SoundEvent HEARTBEAT = new SoundEvent(
        Identifier.fromNamespaceAndPath("mutually-assured-destruction", "heartbeat"),
        Optional.of(50.0F)
    );
    public static final SoundEvent DRIPPING = new SoundEvent(
        Identifier.fromNamespaceAndPath("mutually-assured-destruction", "dripping"),
        Optional.of(25.0F)
    );



    private int heartbeatTimer = 0;
    private int drippingTimer = 0;

    @Override
    public void baseTick() {
        super.baseTick();
        heartbeatTimer--;
        drippingTimer--;
        if (heartbeatTimer <= 0) {
            if (!this.level().isClientSide()) {
                double maxRange = 50.0;
                for (Player player : this.level().players()) {
                    double distance = this.distanceTo(player);
                    if (distance <= maxRange) {
                        float volume = (float)(1.0 - (distance / maxRange));
                        volume = Math.max(0.0F, Math.min(1.0F, volume));
                        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ProphetEntity.HEARTBEAT),
                            SoundSource.HOSTILE,
                            this.getX(), this.getY(), this.getZ(),
                            volume,
                            1.0F,
                            this.level().getRandom().nextLong()
                        );
                        ((ServerPlayer) player).connection.send(packet);
                    } 
                    // else if(distance <= maxRange * 2) {
                    //     ClientboundSoundPacket packet = new ClientboundSoundPacket(
                    //         BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ProphetEntity.HEARTBEAT),
                    //         SoundSource.HOSTILE,
                    //         this.getX(), this.getY(), this.getZ(),
                    //         0.1F,
                    //         1.0F,
                    //         this.level().getRandom().nextLong()
                    //     );
                    //     ((ServerPlayer) player).connection.send(packet);

                    // }
                    
                }
            }
            heartbeatTimer = 39;
        }

        if (drippingTimer <= 0) {
            if (!this.level().isClientSide()) {
                double maxRange = 40.0;
                for (Player player : this.level().players()) {
                    double distance = this.distanceTo(player);
                    if (distance <= maxRange) {
                        float volume = (float)(1.0 - (distance / maxRange));
                        volume = Math.max(0.5F, Math.min(1.0F, volume));
                        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ProphetEntity.DRIPPING),
                            SoundSource.HOSTILE,
                            this.getX(), this.getY(), this.getZ(),
                            volume,
                            1.0F,
                            this.level().getRandom().nextLong()
                        );
                        ((ServerPlayer) player).connection.send(packet);
                    }
                    
                }
            }
            drippingTimer = 259;
        }
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (source.getEntity() instanceof Player player) {
            player.addEffect(new MobEffectInstance(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(RedDarknessEffect.RED_DARKNESS),
                //MobEffects.DARKNESS,
                2400,
                0
            ));
            MutuallyAssuredDestruction.RED_RAIN_PLAYERS.put(player.getUUID(), player.level().getGameTime() + 2400);
            player.sendOverlayMessage(Component.literal("A Prophet of Vormoth is slain!"));
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        ItemEntity drop = new ItemEntity(
            level,
            this.getX(), this.getY(), this.getZ(),
            new ItemStack(MutuallyAssuredDestruction.ALTAR_FRAGMENT)
        );
        level.addFreshEntity(drop);
    }

    public static boolean checkMobSpawnRules(final EntityType<? extends Mob> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos, final RandomSource random) {
        return level.getMaxLocalRawBrightness(pos) <= 7 && level.getDifficulty() != Difficulty.PEACEFUL;
    }

    public static void initialize() {

    }

}