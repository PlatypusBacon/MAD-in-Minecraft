package bunger.group.tyler.entity;

import bunger.group.tyler.data.StructureEventData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

public class GodEntity extends Mob {

    private boolean hasLaunched = false;
    private boolean hasBeenSeen = false;
    private int launchCountdown = -1;
    private int despawnTicks = -1;
    private static final double HOVER_HEIGHT = 0.3;
    private ServerPlayer trackedPlayer = null;
    private static final int PAUSE_TICKS = 60;
    private static final double RISE_SPEED = 0.05;
    private Vec3 frozenPos = null;
    private float lastYaw = 0;
    private int lastYawTick = 0;
    private int turningTicks = 0;
    private static final float MIN_DELTA = 2.0f;     // ignore tiny jitter
    private static final float SPEED_THRESHOLD = 8.0f;
    private static final int REQUIRED_TICKS = 2;

    public GodEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.setSilent(true);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }
    private boolean isPlayerTurning(Player player) {
        float currentYaw = player.getYRot();

        // FIRST RUN FIX: initialize properly
        if (lastYawTick == 0) {
            lastYaw = currentYaw;
            lastYawTick = this.tickCount;
            return false;
        }

        float deltaYaw = Math.abs(currentYaw - lastYaw);

        // handle wraparound
        if (deltaYaw > 180) {
            deltaYaw = 360 - deltaYaw;
        }

        int deltaTime = this.tickCount - lastYawTick;
        if (deltaTime <= 0) deltaTime = 1;

        float yawSpeed = deltaYaw / deltaTime;

        // update history
        lastYaw = currentYaw;
        lastYawTick = this.tickCount;

        // ignore tiny noise completely
        if (deltaYaw < MIN_DELTA) {
            turningTicks = 0;
            return false;
        }

        // accumulate turning over time
        if (yawSpeed > SPEED_THRESHOLD) {
            turningTicks++;
        } else {
            turningTicks = 0;
        }

        return turningTicks >= REQUIRED_TICKS;
    }
    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        if (despawnTicks >= 0) {
            despawnTicks--;
            if (despawnTicks <= 0) {
                this.discard();
                StructureEventData.get((ServerLevel) level()).setEventComplete();
            }
            return;
        }

        if (hasLaunched) return;

        Player nearest = trackedPlayer != null && trackedPlayer.isAlive()
                ? trackedPlayer
                : level().getNearestPlayer(this, 64);
        if (nearest == null) return;

        if (!hasBeenSeen) {
            // god is always directly behind the player's current look direction
            Vec3 lookVec = nearest.getLookAngle();

            // behind = exact opposite of look direction, horizontal only
            Vec3 targetPos = nearest.position()
                    .add(-lookVec.x * 4.0, 0.0, -lookVec.z * 4.0);

            // height
            BlockPos below = BlockPos.containing(targetPos.x, targetPos.y, targetPos.z);
            int groundY = below.getY();
            for (int i = 0; i < 10; i++) {
                BlockPos check = new BlockPos(below.getX(), groundY - i, below.getZ());
                if (!level().getBlockState(check).isAir()) {
                    groundY = check.getY() + 1;
                    break;
                }
            }

            this.setPos(targetPos.x, groundY + HOVER_HEIGHT, targetPos.z);

            // face toward player
            double fdx = nearest.getX() - this.getX();
            double fdz = nearest.getZ() - this.getZ();
            float yaw = (float)(Math.toDegrees(Math.atan2(-fdx, fdz)));
            this.setYRot(yaw);
            this.yRotO = yaw;
            this.setYHeadRot(yaw);

            if (isPlayerTurning(nearest)) {
                hasBeenSeen = true;
                launchCountdown = PAUSE_TICKS;
                frozenPos = this.position();
            }
        } else {
            // stay frozen in place during pause
            if (frozenPos != null) {
                this.setPos(frozenPos.x, frozenPos.y, frozenPos.z);
            }

            // keep facing player during pause
            double fdx = nearest.getX() - this.getX();
            double fdz = nearest.getZ() - this.getZ();
            float yaw = (float)(Math.toDegrees(Math.atan2(-fdx, fdz)));
            this.setYRot(yaw);
            this.yRotO = yaw;
            this.setYHeadRot(yaw);

            launchCountdown--;
            if (launchCountdown == 0) {
                launchPlayer(nearest);
            }
        }
    }

    private boolean isPlayerLookingAtMe(Player player) {
        Vec3 toGod = this.position()
                .subtract(player.getEyePosition()).normalize();
        Vec3 lookVec = player.getLookAngle();
        return lookVec.dot(toGod) > 0.6;
    }

    public void setTrackedPlayer(ServerPlayer player) {
        this.trackedPlayer = player;
    }
    private void launchPlayer(Player player) {
        hasLaunched = true;
        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(look.x * 3.0, 4.5, look.z * 3.0);
        player.hurtMarked = true;
        despawnTicks = 60;
    }

    @Override
    protected void registerGoals() {}
}