package bunger.group.tyler.event.god;


import bunger.group.tyler.event.TickScheduler;
import bunger.group.tyler.sound.ModSounds;
import bunger.group.tyler.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import bunger.group.tyler.data.StructureEventData;

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
            openDoor(level);

            // spawn god 5 seconds after "GOD IS HERE", then respawn behind
            // player every second until they look at it
            final long spawnStartTick = level.getGameTime() + 5L * TICKS_PER_SECOND;
            scheduleFutureTick(level, spawnStartTick, () ->
                    scheduleGodRespawnLoop(level, origin));

            active = false;
        });
    }

    /**
     * Spawns god behind the player, then schedules a 1-second check.
     * If the player still hasn't looked at god, discards the current one
     * and spawns a new one behind them. Stops once GodEntity sets hasBeenSeen.
     */
    public static void scheduleGodRespawnLoop(ServerLevel level, BlockPos origin) {
        StructureEventData data = StructureEventData.get(level);
        if (data.isGodSpawned()) return; // GodEntity already triggered launch

        // find nearest player
        var target = level.players().stream()
                .min((a, b) -> Double.compare(
                        a.distanceToSqr(origin.getX(), origin.getY(), origin.getZ()),
                        b.distanceToSqr(origin.getX(), origin.getY(), origin.getZ())));

        if (target.isEmpty()) {
            // no player found, retry in 1s
            scheduleFutureTick(level, level.getGameTime() + 20L, () ->
                    scheduleGodRespawnLoop(level, origin));
            return;
        }

        ServerPlayer player = (ServerPlayer) target.get();

        // discard any existing god entities near origin
        var existing = level.getEntitiesOfClass(
                bunger.group.entity.GodEntity.class,
                new net.minecraft.world.phys.AABB(
                        data.getStructureOrigin(), data.getStructureEnd()).inflate(8.0)
        );
        existing.forEach(net.minecraft.world.entity.Entity::discard);

        // spawn behind the player: opposite of their look direction, 4 blocks away
        var look = player.getLookAngle();
        // "behind" = negate horizontal look, keep Y at player feet + 1
        var spawnPos = player.position()
                .add(-look.x * 4.0, 0.0, -look.z * 4.0);

        var god = bunger.group.entity.ModEntities.GOD.create(level);
        if (god == null) return;

        god.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, 0f, 0f);

        // face the god toward the player
        double dx = player.getX() - spawnPos.x;
        double dz = player.getZ() - spawnPos.z;
        float yaw = (float)(Math.toDegrees(Math.atan2(-dx, dz)));
        god.setYRot(yaw);
        god.yRotO = yaw;
        god.setYHeadRot(yaw);

        level.addFreshEntity(god);

        // check again in 1 second — if god still not seen, respawn behind player
        scheduleFutureTick(level, level.getGameTime() + 20L, () -> {
            if (!data.isGodSpawned()) {
                scheduleGodRespawnLoop(level, origin);
            }
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
        var savedTop    = data.getDoorTopState();

        if (savedBottom != null) level.setBlock(bottomPos, savedBottom, 3);
        if (savedTop    != null) level.setBlock(topPos,    savedTop,    3);
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