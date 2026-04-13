package bunger.group.event;

import bunger.group.sound.ModSounds;
import bunger.group.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import bunger.group.data.StructureEventData;

public class God {

    private static boolean active = false;

    public static final int COUNTDOWN_SECONDS = 120; // 2 minutes
    private static final int MUSIC_START_SECOND = 52; // music starts when 52s remain
    private static final int TICKS_PER_SECOND = 20;

    public static void start(ServerLevel level, BlockPos origin) {
        if (active) return;
        active = true;

        // broadcast opening message with subtitle
        broadcastTitle(level, origin,
                "§4§lGOD IS COMING",
                Component.literal("§7§oI must prepare to meet god"),
                64);

        // close and lock the squirrel house door
        closeDoor(level, origin);

        // schedule each countdown second
        for (int i = 0; i <= COUNTDOWN_SECONDS; i++) {
            final int secondsLeft = COUNTDOWN_SECONDS - i;
            final long triggerTick = level.getGameTime() + (long)(i * TICKS_PER_SECOND);

            scheduleFutureTick(level, triggerTick, () ->
                    onCountdownTick(level, origin, secondsLeft));
        }

        // schedule music when 52 seconds remain
        final long musicTick = level.getGameTime()
                + (long)((COUNTDOWN_SECONDS - MUSIC_START_SECOND) * TICKS_PER_SECOND);
        scheduleFutureTick(level, musicTick, () ->
                level.playSound(null, origin, ModSounds.GOD_IS_COMING,
                        SoundSource.RECORDS, 2.0f, 1.0f));

        // schedule GOD_IS_HERE track and spawn at countdown end
        final long endTick = level.getGameTime()
                + (long)(COUNTDOWN_SECONDS * TICKS_PER_SECOND);
        scheduleFutureTick(level, endTick, () -> {
            level.playSound(null, origin, ModSounds.GOD_IS_HERE,
                    SoundSource.RECORDS, 2.0f, 1.0f);
            scheduleFutureTick(level,
                    level.getGameTime() + 200L,
                    () -> spawnGod(level, origin));
            active = false;
        });
    }

    private static void scheduleFutureTick(ServerLevel level, long targetTick, Runnable task) {
        TickScheduler.schedule(level, targetTick, task);
    }

    private static void onCountdownTick(ServerLevel level, BlockPos origin, int secondsLeft) {
        if (secondsLeft > 0) {
            int minutes = secondsLeft / 60;
            int seconds = secondsLeft % 60;
            String timerText = String.format("%d:%02d", minutes, seconds);

            // No distance filter — all players get the title
            for (var p : level.players()) {
                ServerPlayer sp = (ServerPlayer) p;
                sp.connection.send(new ClientboundSetTitlesAnimationPacket(0, 25, 5));
                sp.connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal("§4§lGOD IS COMING")));
                sp.connection.send(new ClientboundSetSubtitleTextPacket(
                        Component.literal("§f§l" + timerText)));
            }
        } else {
            for (var p : level.players()) {
                ServerPlayer sp = (ServerPlayer) p;
                sp.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
                sp.connection.send(new ClientboundSetTitleTextPacket(
                        Component.literal("§4§lGOD IS HERE")));
                sp.connection.send(new ClientboundSetSubtitleTextPacket(
                        Component.literal("§7§o!!!!!!!!!!!!!!")));
            }
        }
    }

    private static void spawnGod(ServerLevel level, BlockPos origin) {
        StructureEventData data = StructureEventData.get(level);
        if (data.isGodSpawned()) return;
        data.setGodSpawned();

        // Try nearest player first, fall back to any player
        var target = level.players().stream()
                .min((a, b) -> Double.compare(
                        a.distanceToSqr(origin.getX(), origin.getY(), origin.getZ()),
                        b.distanceToSqr(origin.getX(), origin.getY(), origin.getZ())
                ));

        target.ifPresent(player -> {
            var look = player.getLookAngle().scale(-5);
            var spawnPos = player.position().add(look);

            var god = bunger.group.entity.ModEntities.GOD.create(level);
            if (god == null) return;

            god.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, 0f, 0f);
            level.addFreshEntity(god);
        });
    }
    private static void closeDoor(ServerLevel level, BlockPos origin) {
        BlockPos bottomPos = StructurePlacer.getDoorPos(level);
        BlockPos topPos = bottomPos.above();

        System.out.println("Closing door at: " + bottomPos + " / " + topPos);

        for (BlockPos doorPos : new BlockPos[]{bottomPos, topPos}) {
            var state = level.getBlockState(doorPos);
            if (state.getBlock() instanceof net.minecraft.world.level.block.DoorBlock) {
                level.setBlock(doorPos,
                        state.setValue(net.minecraft.world.level.block.DoorBlock.OPEN, false)
                                .setValue(net.minecraft.world.level.block.DoorBlock.POWERED, true),
                        3);
            }
        }
    }
    private static Component getSubtitleForTime(int secondsLeft) {
        if (secondsLeft > 110) return Component.literal("§7§oI must prepare to meet God.");
        return Component.literal("§4§");
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