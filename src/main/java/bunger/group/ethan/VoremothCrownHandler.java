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
import net.minecraft.world.entity.projectile.ProjectileUtil;
// import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bunger.group.MutuallyAssuredDestruction;

public class VoremothCrownHandler {

    private static final Map<UUID, Integer> LASER_TIMERS = new HashMap<>();
    private static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
    private static final Map<UUID, Integer> LOOK_TIMERS = new HashMap<>();
    private static final Map<UUID, Integer> LOOK_TARGET_IDS = new HashMap<>();
    private static final Map<UUID, Integer> LOCKED_TARGETS = new HashMap<>();
    private static final int LOOK_REQUIRED = 30;
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
                        LOOK_TIMERS.remove(uuid);
                        LOOK_TARGET_IDS.remove(uuid);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }  else {
                        // constant head glow
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

                    // handle cooldown
                    if (COOLDOWNS.containsKey(uuid)) {
                        int cd = COOLDOWNS.get(uuid) - 1;
                        if (cd <= 0) {
                            COOLDOWNS.remove(uuid);
                        } else {
                            COOLDOWNS.put(uuid, cd);
                        }
                        LOOK_TIMERS.remove(uuid);
                        LOOK_TARGET_IDS.remove(uuid);
                        LOCKED_TARGETS.remove(uuid);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }

                    // if already locked on, skip look phase entirely and just charge
                    if (LOCKED_TARGETS.containsKey(uuid)) {
                        int lockedId = LOCKED_TARGETS.get(uuid);
                        LivingEntity lockedTarget = null;
                        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, 
                                player.getBoundingBox().inflate(60.0))) {
                            if (e.getId() == lockedId) {
                                lockedTarget = e;
                                break;
                            }
                        }

                        // target died or disappeared
                        if (lockedTarget == null || !lockedTarget.isAlive()) {
                            LOCKED_TARGETS.remove(uuid);
                            LASER_TIMERS.remove(uuid);
                            ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                            continue;
                        }

                        int timer = LASER_TIMERS.getOrDefault(uuid, 0) + 1;
                        LASER_TIMERS.put(uuid, timer);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(lockedId, timer));

                        if (timer <= 1) {
                            level.playSound(null,
                                player.getX(), player.getY(), player.getZ(),
                                MutuallyAssuredDestruction.VOREMOTH_CHARGE,
                                SoundSource.AMBIENT, 0.5F, 0.9F);
                        }

                        if (timer >= WARMUP) {
                            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(
                                level, EntitySpawnReason.TRIGGERED);
                            if (lightning != null) {
                                lightning.setPos(lockedTarget.getX(), lockedTarget.getY(), lockedTarget.getZ());
                                lightning.setVisualOnly(false);
                                level.addFreshEntity(lightning);
                            }
                            LASER_TIMERS.remove(uuid);
                            LOCKED_TARGETS.remove(uuid);
                            COOLDOWNS.put(uuid, COOLDOWN);
                            ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        }
                        continue;
                    }

                    // look phase - find what player is looking at
                    LivingEntity target = getTargetedEntity(player, level);

                    if (target == null) {
                        LOOK_TIMERS.remove(uuid);
                        LOOK_TARGET_IDS.remove(uuid);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }

                    int lookedAtId = target.getId();
                    int previousLookTarget = LOOK_TARGET_IDS.getOrDefault(uuid, -1);

                    if (lookedAtId != previousLookTarget) {
                        LOOK_TIMERS.put(uuid, 0);
                        LOOK_TARGET_IDS.put(uuid, lookedAtId);
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(-1, 0));
                        continue;
                    }

                    int lookTimer = LOOK_TIMERS.getOrDefault(uuid, 0) + 1;
                    LOOK_TIMERS.put(uuid, lookTimer);

                    if (lookTimer < LOOK_REQUIRED) {
                        ServerPlayNetworking.send(player, new VoremothCrownPacket(lookedAtId, -lookTimer));
                        continue;
                    }

                    // lock on complete - store the locked target and clear look state
                    LOCKED_TARGETS.put(uuid, lookedAtId);
                    LOOK_TIMERS.remove(uuid);
                    LOOK_TARGET_IDS.remove(uuid);
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


    private static LivingEntity getTargetedEntity(ServerPlayer player, ServerLevel level) {
        double range = 50.0;
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(range));
        AABB searchBox = player.getBoundingBox().expandTowards(look.scale(range)).inflate(1.0);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
            player,
            start,
            end,
            searchBox,
            entity -> entity instanceof LivingEntity && entity != player && !(entity instanceof ServerPlayer),
            range * range
        );

        return hitResult != null ? (LivingEntity) hitResult.getEntity() : null;
    }
}