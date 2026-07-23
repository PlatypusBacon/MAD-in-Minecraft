package bunger.group.client.ethan;

import bunger.group.ethan.VoremothCrownPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class VoremothCrownClientHandler {

    private static final Map<Integer, int[]> PLAYER_TARGETS = new HashMap<>();

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(VoremothCrownPacket.TYPE, (payload, context) -> {
            int targetId = payload.targetEntityId();
            int timer = payload.laserTimer();
            int playerId = context.player().getId();

            if (targetId == -1) {
                PLAYER_TARGETS.remove(playerId);
            } else {
                PLAYER_TARGETS.put(playerId, new int[]{targetId, timer});
            }
        });

    ClientTickEvents.END_CLIENT_TICK.register(client -> {
        if (client.level == null) return;

        for (Map.Entry<Integer, int[]> entry : PLAYER_TARGETS.entrySet()) {
            Entity player = client.level.getEntity(entry.getKey());
            int targetId = entry.getValue()[0];
            int timer = entry.getValue()[1];

            if (player == null) continue;

            Entity target = client.level.getEntity(targetId);
            if (target == null) continue;

            if (timer < 0) {
                // locking on phase
                float progress = Math.abs(timer) / 40.0F;
                for (int i = 0; i < 3; i++) {
                    double angle = (client.level.getGameTime() * 0.2) + (i * Math.PI * 2 / 3);
                    double radius = 1.5 * (1.0 - progress); // shrinks as lock-on completes
                    double x = target.getX() + Math.cos(angle) * radius;
                    double z = target.getZ() + Math.sin(angle) * radius;
                    client.level.addParticle(ParticleTypes.FALLING_LAVA,
                        x, target.getEyeY(), z, 0, 0.05, 0);
                }
            } else {
                // charging phase
                Vec3 start = new Vec3(player.getEyePosition().x, player.getEyePosition().y + 1, player.getEyePosition().z);
                Vec3 end = target.getEyePosition();
                Vec3 direction = end.subtract(start);
                Vec3 step = direction.normalize().scale(0.5);
                int numParticles = (int)(direction.length() / 0.5);
                for (int i = 0; i < numParticles; i++) {
                    Vec3 pos = start.add(step.scale(i));
                    client.level.addParticle(ParticleTypes.GLOW, pos.x, pos.y, pos.z, 0, 0, 0);
                }
            }
        }
    });
    }
}