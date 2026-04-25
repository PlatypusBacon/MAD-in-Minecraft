package bunger.group.tyler.combat;

import bunger.group.tyler.event.TickScheduler;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public class ParryHandler {

    private static final int PARRY_WINDOW_TICKS = 8;

    private static final Set<UUID> PARRYING =
            Collections.synchronizedSet(
                    Collections.newSetFromMap(new WeakHashMap<>())
            );

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof Player player)) return true;
            if (!PARRYING.contains(player.getUUID())) return true;

            PARRYING.remove(player.getUUID());

            if (source.getDirectEntity() instanceof LivingEntity attacker) {
                attacker.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 3));
                attacker.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 60, 3));

                double dx = attacker.getX() - player.getX();
                double dz = attacker.getZ() - player.getZ();
                double len = Math.sqrt(dx * dx + dz * dz);
                if (len > 0) {
                    attacker.setDeltaMovement(dx / len * 1.2, 0.4, dz / len * 1.2);
                    attacker.hurtMarked = true;
                }
            }

            return false;
        });
    }

    public static void beginParry(Player player) {
        // Level must be a ServerLevel to read game time — use OVERWORLD as canonical clock,
        // matching TickScheduler's own Level.OVERWORLD reference.
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        UUID id = player.getUUID();
        PARRYING.add(id);

        long targetTick = serverLevel.getGameTime() + PARRY_WINDOW_TICKS;
        TickScheduler.schedule(serverLevel, targetTick, () -> PARRYING.remove(id));
    }

    public static boolean isParrying(Player player) {
        return PARRYING.contains(player.getUUID());
    }
}