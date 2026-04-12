package bunger.group.ethan;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;


public class ProphetEntity extends PathfinderMob {

    public ProphetEntity(EntityType<? extends ProphetEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
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
                    } else if(distance <= maxRange * 2) {
                        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ProphetEntity.HEARTBEAT),
                            SoundSource.HOSTILE,
                            this.getX(), this.getY(), this.getZ(),
                            0.1F,
                            1.0F,
                            this.level().getRandom().nextLong()
                        );
                        ((ServerPlayer) player).connection.send(packet);

                    }
                    
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

    public static void initialize() {

    }

}