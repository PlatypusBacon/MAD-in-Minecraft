package bunger.group.tyler3;

import bunger.group.tyler.entity.ModEntities;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StripperDetector {

    // ---- Detection config ----
    private static final int Y_THRESHOLD = 15;
    private static final int STREAK_THRESHOLD = 30;
    private static final int REPEAT_INTERVAL = 5;
    private static final int TICKS_BETWEEN_BREAKS = 280;
    private static final double FRONT_DOT_MIN = 0.5;
    private static final int MAX_LATERAL_DEVIATION = 1;
    private static final int TICK_INTERVAL = 10;

    // ---- Tunnel refill config ----
    private static final double FILL_CHANCE = 0.06;
    private static final double BLINDNESS_CHANCE = 0.08;
    private static final double DOG_CHANCE = 0.10;
    private static final int MIN_FILL_STREAK = 10;
    private static final int MIN_FILL_DISTANCE = 3;
    private static final int MAX_FILL_DISTANCE = 8;
    private static final int DEEPSLATE_Y_LEVEL = 0;

    private static final Map<UUID, MiningStreak> STREAKS = new ConcurrentHashMap<>();
    private static final Random RANDOM = new Random();
    private static int tickCounter = 0;

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(StripperDetector::onBlockBreak);
        ServerTickEvents.END_SERVER_TICK.register(StripperDetector::onServerTick);
        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> STREAKS.remove(handler.getPlayer().getUUID())
        );
    }

    private static void onBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (level.isClientSide() || !(player instanceof ServerPlayer sp)) return;

        if (pos.getY() >= Y_THRESHOLD) {
            STREAKS.remove(player.getUUID());
            return;
        }

        Direction facing = player.getDirection();
        long now = level.getGameTime();

        MiningStreak streak = STREAKS.computeIfAbsent(player.getUUID(), id -> new MiningStreak());

        boolean inFront = isInFront(player, pos, facing);
        boolean withinTime = now - streak.lastBreakTick <= TICKS_BETWEEN_BREAKS;
        boolean sameDirection = streak.direction == facing;

        if (inFront && withinTime && sameDirection) {
            streak.count++;
            maybeFillTunnelBehind(level, sp, streak);
        } else {
            streak.count = 1;
            streak.direction = facing;
            streak.originPos = player.blockPosition();
        }

        streak.lastBreakTick = now;

        boolean crossedThreshold = streak.count == STREAK_THRESHOLD;
        boolean hitRepeat = REPEAT_INTERVAL > 0
                && streak.count > STREAK_THRESHOLD
                && (streak.count - STREAK_THRESHOLD) % REPEAT_INTERVAL == 0;

        if (crossedThreshold || hitRepeat) {
            StripperEvent.DETECTED.invoker().onStripMiningDetected(sp, pos, facing, streak.count);
        }
    }

    private static void onServerTick(MinecraftServer server) {
        if (++tickCounter % TICK_INTERVAL != 0) return;
        if (STREAKS.isEmpty()) return;

        Iterator<Map.Entry<UUID, MiningStreak>> it = STREAKS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, MiningStreak> entry = it.next();
            ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
            if (player == null) {
                it.remove();
                continue;
            }

            MiningStreak streak = entry.getValue();
            if (streak.originPos == null) continue;

            BlockPos current = player.blockPosition();
            if (current.getY() >= Y_THRESHOLD) {
                it.remove();
                continue;
            }

            int forwardProgress = forwardOffset(streak.originPos, current, streak.direction);
            int lateralDeviation = lateralOffset(streak.originPos, current, streak.direction);

            if (forwardProgress < 0 || lateralDeviation > MAX_LATERAL_DEVIATION) {
                streak.count = 0;
                streak.originPos = null;
            }
        }
    }

    private static boolean isInFront(Player player, BlockPos pos, Direction facing) {
        double eyeX = player.getX();
        double eyeY = player.getEyeY();
        double eyeZ = player.getZ();

        double dx = (pos.getX() + 0.5) - eyeX;
        double dy = (pos.getY() + 0.5) - eyeY;
        double dz = (pos.getZ() + 0.5) - eyeZ;

        double horizLenSq = dx * dx + dz * dz;
        if (horizLenSq < 1.0e-6) return true;

        double horizLen = Math.sqrt(horizLenSq);
        double dot = (dx * facing.getStepX() + dz * facing.getStepZ()) / horizLen;

        return dot >= FRONT_DOT_MIN && Math.abs(dy) <= 1.5 && horizLen <= 2.0;
    }

    private static void maybeFillTunnelBehind(Level level, ServerPlayer player, MiningStreak streak) {
        if (streak.count < MIN_FILL_STREAK) return;
        if (RANDOM.nextDouble() >= FILL_CHANCE) return;
        int maxDistance = Math.min(streak.count - 1, MAX_FILL_DISTANCE);
        if (maxDistance < MIN_FILL_DISTANCE) return;

        int distance = MIN_FILL_DISTANCE + RANDOM.nextInt(maxDistance - MIN_FILL_DISTANCE + 1);

        BlockPos fillFeet = player.blockPosition().relative(streak.direction.getOpposite(), distance);
        BlockPos fillHead = fillFeet.above();

        fillIfAir(level, fillFeet);
        fillIfAir(level, fillHead);
        if (RANDOM.nextDouble() >= BLINDNESS_CHANCE) return;
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false));

        if (RANDOM.nextDouble() >= DOG_CHANCE) return;
        var dog = ModEntities.DOG.create(level, EntitySpawnReason.MOB_SUMMONED);
        dog.setTrackedPlayer(player);
        dog.setPos(Vec3.atLowerCornerOf(fillFeet));
        DifficultyInstance dil = null;
        if (level instanceof ServerLevel sl) {
            dil = sl.getCurrentDifficultyAt(fillFeet);
        };

        dog.finalizeSpawn(
                (ServerLevelAccessor) level,
                dil,
                EntitySpawnReason.MOB_SUMMONED,
                null
        );
        level.addFreshEntity(dog);

    }

    private static void fillIfAir(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).isAir()) return;
        BlockState fillState = (pos.getY() < DEEPSLATE_Y_LEVEL ? Blocks.DEEPSLATE : Blocks.STONE).defaultBlockState();
        level.setBlockAndUpdate(pos, fillState);
    }

    private static int forwardOffset(BlockPos origin, BlockPos current, Direction facing) {
        return switch (facing) {
            case NORTH -> origin.getZ() - current.getZ();
            case SOUTH -> current.getZ() - origin.getZ();
            case WEST -> origin.getX() - current.getX();
            case EAST -> current.getX() - origin.getX();
            default -> 0;
        };
    }

    private static int lateralOffset(BlockPos origin, BlockPos current, Direction facing) {
        return switch (facing) {
            case NORTH, SOUTH -> Math.abs(current.getX() - origin.getX());
            case EAST, WEST -> Math.abs(current.getZ() - origin.getZ());
            default -> 0;
        };
    }

    private static class MiningStreak {
        Direction direction;
        int count = 0;
        long lastBreakTick = 0;
        BlockPos originPos;
    }
}