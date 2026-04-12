package bunger.group.event;

import bunger.group.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class God {

    private static boolean active = false;

    public static final int COUNTDOWN_SECONDS = 30;
    private static final int TICKS_PER_SECOND = 20;

    public static void start(ServerLevel level, BlockPos origin) {
        if (active) return;
        active = true;

        // play buildup track
        level.playSound(null, origin, ModSounds.GOD_IS_COMING,
                SoundSource.RECORDS, 2.0f, 1.0f);

        // broadcast opening message
        broadcastMessage(level, origin, "§4§lTHE OFFERING HAS BEEN MADE", 64);

        // schedule each countdown second
        for (int i = 0; i <= COUNTDOWN_SECONDS; i++) {
            final int secondsLeft = COUNTDOWN_SECONDS - i;
            final long triggerTick = level.getGameTime() + (long)(i * TICKS_PER_SECOND);

            scheduleFutureTick(level, triggerTick, () ->
                    onCountdownTick(level, origin, secondsLeft));
        }

        // schedule main track after buildup ends
        final long mainTrackTick = level.getGameTime()
                + (long)(COUNTDOWN_SECONDS * TICKS_PER_SECOND);
        scheduleFutureTick(level, mainTrackTick, () -> {
            level.playSound(null, origin, ModSounds.GOD_IS_HERE,
                    SoundSource.RECORDS, 2.0f, 1.0f);
            active = false;
        });
    }

    private static void scheduleFutureTick(ServerLevel level,
                                           long targetGameTime,
                                           Runnable task) {
        // register a recurring check that fires the task once the game time is reached
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
                .END_SERVER_TICK.register(server -> {
                    if (level.getGameTime() >= targetGameTime) {
                        task.run();
                        // note: Fabric event listeners can't easily unregister,
                        // so we guard with the active flag and a one-shot boolean
                    }
                });
    }

    private static void onCountdownTick(ServerLevel level,
                                        BlockPos origin,
                                        int secondsLeft) {
        if (secondsLeft > 0) {
            level.players().stream()
                    .filter(p -> p.distanceToSqr(
                            origin.getX(), origin.getY(), origin.getZ()) < 4096)
                    .forEach(p -> {
                        ServerPlayer sp = (ServerPlayer) p;
                        sp.connection.send(
                                new ClientboundSetTitlesAnimationPacket(0, 25, 5));
                        sp.connection.send(
                                new ClientboundSetTitleTextPacket(
                                        Component.literal("§4§l" + secondsLeft)));
                        sp.connection.send(
                                new ClientboundSetSubtitleTextPacket(
                                        getSubtitleForTime(secondsLeft)));
                    });
        } else {
            broadcastTitle(level, origin,
                    "§4§lIT IS HERE",
                    Component.literal("§7§oThere is no escape."),
                    64);
            active = false;
        }
    }

    private static Component getSubtitleForTime(int secondsLeft) {
        if (secondsLeft > 20) return Component.literal("§7§oIt watches. It waits.");
        if (secondsLeft > 10) return Component.literal("§7§oYou should not have done this.");
        if (secondsLeft > 5)  return Component.literal("§7§oRun.");
        return Component.literal("§4§oToo late.");
    }

    private static void broadcastMessage(ServerLevel level, BlockPos origin,
                                         String message, double range) {
        level.players().stream()
                .filter(p -> p.distanceToSqr(
                        origin.getX(), origin.getY(), origin.getZ()) < range * range)
                .forEach(p -> p.sendSystemMessage(Component.literal(message)));
    }

    private static void broadcastTitle(ServerLevel level, BlockPos origin,
                                       String title, Component subtitle,
                                       double range) {
        level.players().stream()
                .filter(p -> p.distanceToSqr(
                        origin.getX(), origin.getY(), origin.getZ()) < range * range)
                .forEach(p -> {
                    ServerPlayer sp = (ServerPlayer) p;
                    sp.connection.send(
                            new ClientboundSetTitlesAnimationPacket(10, 60, 20));
                    sp.connection.send(
                            new ClientboundSetTitleTextPacket(
                                    Component.literal(title)));
                    sp.connection.send(
                            new ClientboundSetSubtitleTextPacket(subtitle));
                });
    }
}