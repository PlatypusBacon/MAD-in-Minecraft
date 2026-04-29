package bunger.group.ethan;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
// import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bunger.group.MutuallyAssuredDestruction;

public class VoremothCrownHandler {

    private static final Map<UUID, Integer> LASER_TIMERS = new HashMap<>();
    private static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
    private static final int WARMUP = 60;
    private static final int COOLDOWN = 100;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerLevel level : server.getAllLevels()) {
                for (ServerPlayer player : level.players()) {
                    UUID uuid = player.getUUID();

                    // check if wearing the crown
                    ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                    if (!(helmet.getItem() == (MutuallyAssuredDestruction.VOREMOTH_CROWN))) {
                        LASER_TIMERS.remove(uuid);
                        COOLDOWNS.remove(uuid);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }  else {
                        //System.out.println("head glow");
                        if (player.tickCount % 8 == 0) {
                            level.sendParticles(
                                ParticleTypes.GLOW,
                                player.getEyePosition().x,
                                player.getEyePosition().y + 0.5,
                                player.getEyePosition().z,
                                1, 0, 0, 0, 0.01
                            );
                        }
                    }

                    // public static final Item vitem = MutuallyAssuredDestruction.VOREMOTH_CROWN;

                    // handle cooldown
                    if (COOLDOWNS.containsKey(uuid)) {
                        int cd = COOLDOWNS.get(uuid) - 1;
                        if (cd <= 0) {
                            COOLDOWNS.remove(uuid);
                        } else {
                            COOLDOWNS.put(uuid, cd);
                        }
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }

                    // find nearest entity that isn't the player
                    LivingEntity target = findNearestTarget(player, level);
                    if (target == null) {
                        LASER_TIMERS.remove(uuid);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }

                    // increment laser timer
                    int timer = LASER_TIMERS.getOrDefault(uuid, 0) + 1;
                    LASER_TIMERS.put(uuid, timer);

                    // sync to client for particle beam rendering
                    // (handled client side using similar approach to voremoth)
                    // after incrementing timer and finding target, add:
                    if (target != null) {
                        VoremothCrownPacket packet = new VoremothCrownPacket(target.getId(), timer);
                        ServerPlayNetworking.send(player, packet);
                    } 
                    // else {
                    //     // send -1 to clear the beam on client
                    //     ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                    // }
                    if (timer <= 1) {
                        level.playSound(
                                null,
                                player.getX(), player.getY(), player.getZ(),
                                MutuallyAssuredDestruction.VOREMOTH_CHARGE,
                                SoundSource.AMBIENT,
                                0.5F,
                                0.9F
                        );
                    }
                    

                    if (timer >= WARMUP) {
                        // fire lightning
                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(
                            level, EntitySpawnReason.TRIGGERED
                        );
                        if (lightning != null) {
                            lightning.setPos(target.getX(), target.getY(), target.getZ());
                            lightning.setVisualOnly(false);
                            level.addFreshEntity(lightning);
                        }
                        LASER_TIMERS.remove(uuid);
                        COOLDOWNS.put(uuid, COOLDOWN);
                    }
                }
            }
        });
    }

    private static LivingEntity findNearestTarget(ServerPlayer player, ServerLevel level) {
        double range = 30.0;
        LivingEntity nearest = null;
        double nearestDist = Double.MAX_VALUE;

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(range))) {
            if (entity == player) continue;
            if (entity instanceof ServerPlayer) continue; // don't target other players
            double dist = entity.distanceTo(player);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = entity;
            }
        }
        return nearest;
    }
}