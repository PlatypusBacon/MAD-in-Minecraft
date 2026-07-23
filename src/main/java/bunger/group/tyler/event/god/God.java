package bunger.group.tyler.event.god;

import bunger.group.tyler.event.TickScheduler;
import bunger.group.tyler.sound.ModSounds;
import bunger.group.tyler.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import bunger.group.tyler.data.StructureEventData;

public class God {

    private static boolean active = false;

    public static final int COUNTDOWN_SECONDS = 120;
    private static final int MUSIC_START_SECOND = 52;
    private static final int TICKS_PER_SECOND = 20;

    public static void start(ServerLevel level, BlockPos origin, ServerPlayer eventPlayer) {
        if (active) return;
        active = true;

        broadcastTitle(level, eventPlayer,
                "§4§lGOD IS COMING",
                Component.literal("§7§oI must prepare to meet god"),
                64);

        closeDoor(level, origin);

        for (int i = 0; i <= COUNTDOWN_SECONDS; i++) {
            final int secondsLeft = COUNTDOWN_SECONDS - i;
            final long triggerTick = level.getGameTime() + (long)(i * TICKS_PER_SECOND);
            scheduleFutureTick(level, triggerTick, () ->
                    onCountdownTick(level, origin, secondsLeft, eventPlayer));
        }

        final long musicTick = level.getGameTime()
                + (long)((COUNTDOWN_SECONDS - MUSIC_START_SECOND) * TICKS_PER_SECOND);
        scheduleFutureTick(level, musicTick, () ->
                playSoundForPlayers(level, ModSounds.GOD_IS_COMING, eventPlayer));

        final long endTick = level.getGameTime()
                + (long)(COUNTDOWN_SECONDS * TICKS_PER_SECOND);
        scheduleFutureTick(level, endTick, () -> {
            playSoundForPlayers(level, ModSounds.GOD_IS_HERE, eventPlayer);
            openDoor(level);

            final long spawnStartTick = level.getGameTime() + 5L * TICKS_PER_SECOND;
            scheduleFutureTick(level, spawnStartTick, () -> {
                // always spawn god behind the event player specifically
                var look = eventPlayer.getLookAngle();
                var spawnPos = eventPlayer.position()
                        .add(-look.x * 4.0, 0.0, -look.z * 4.0);

                var god = bunger.group.tyler.entity.ModEntities.GOD.create(level,
                        net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
                if (god == null) return;

                god.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                god.setTrackedPlayer(eventPlayer);
                level.addFreshEntity(god);
                StructureEventData.get(level).setGodSpawned();
            });

            active = false;
        });
    }

    private static void playSoundForPlayers(ServerLevel level, SoundEvent sound, ServerPlayer eventPlayer) {
        for (var p : level.players()) {
            if (p == eventPlayer || p.distanceToSqr(eventPlayer) < 64 * 64) {
                p.connection.send(new ClientboundSoundPacket(
                        net.minecraft.core.Holder.direct(sound),
                        SoundSource.RECORDS,
                        p.getX(), p.getY(), p.getZ(),
                        1.0f, 1.0f,
                        level.getRandom().nextLong()
                ));
            }
        }
    }

    private static void scheduleFutureTick(ServerLevel level, long targetTick, Runnable task) {
        TickScheduler.schedule(level, targetTick, task);
    }

    private static void onCountdownTick(ServerLevel level, BlockPos origin,
                                        int secondsLeft, ServerPlayer eventPlayer) {
        if (secondsLeft > 0) {
            int minutes = secondsLeft / 60;
            int seconds = secondsLeft % 60;
            String timerText = String.format("%d:%02d", minutes, seconds);

            for (var p : level.players()) {
                ServerPlayer sp = (ServerPlayer) p;
                if (sp == eventPlayer || sp.distanceToSqr(eventPlayer) < 64 * 64) {
                    sp.connection.send(new ClientboundSetTitlesAnimationPacket(0, 25, 5));
                    sp.connection.send(new ClientboundSetTitleTextPacket(
                            Component.literal("§4§lGOD IS COMING")));
                    sp.connection.send(new ClientboundSetSubtitleTextPacket(
                            Component.literal("§f§l" + timerText)));
                }
            }
        } else {
            for (var p : level.players()) {
                ServerPlayer sp = (ServerPlayer) p;
                if (sp == eventPlayer || sp.distanceToSqr(eventPlayer) < 64 * 64) {
                    sp.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
                    sp.connection.send(new ClientboundSetTitleTextPacket(
                            Component.literal("§4§lGOD IS HERE")));
                    sp.connection.send(new ClientboundSetSubtitleTextPacket(
                            Component.literal("§7§o!!!!!!!!!!!!!!")));
                }
            }
        }
    }

    private static void closeDoor(ServerLevel level, BlockPos origin) {
        BlockPos bottomPos = StructurePlacer.getDoorPos(level);
        BlockPos topPos = bottomPos.above();
        System.out.println("Closing door at: " + bottomPos + " / " + topPos);
        StructureEventData data = StructureEventData.get(level);
        data.setDoorBottomState(level.getBlockState(bottomPos));
        data.setDoorTopState(level.getBlockState(topPos));
        level.setBlock(bottomPos,
                net.minecraft.world.level.block.Blocks.BARRIER.defaultBlockState(), 3);
        level.setBlock(topPos,
                net.minecraft.world.level.block.Blocks.BARRIER.defaultBlockState(), 3);
    }

    private static void openDoor(ServerLevel level) {
        StructureEventData data = StructureEventData.get(level);
        BlockPos bottomPos = StructurePlacer.getDoorPos(level);
        BlockPos topPos = bottomPos.above();
        var savedBottom = data.getDoorBottomState();
        var savedTop = data.getDoorTopState();
        if (savedBottom != null) level.setBlock(bottomPos, savedBottom, 3);
        if (savedTop != null) level.setBlock(topPos, savedTop, 3);
    }

    private static void broadcastTitle(ServerLevel level, ServerPlayer eventPlayer,
                                       String title, Component subtitle, double range) {
        for (var p : level.players()) {
            ServerPlayer sp = (ServerPlayer) p;
            if (sp == eventPlayer || sp.distanceToSqr(eventPlayer) < range * range) {
                sp.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
                sp.connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal(title)));
                sp.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
            }
        }
    }
}