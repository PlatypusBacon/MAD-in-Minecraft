package bunger.group.tyler.entity;

import bunger.group.tyler.data.StructureEventData;
import net.minecraft.core.BlockPos;
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
    private static final double HOVER_HEIGHT = 1.0;
    private static final double RISE_SPEED = 0.05;

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

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;

        // handle despawn
        if (despawnTicks >= 0) {
            despawnTicks--;
            if (despawnTicks <= 0) {
                this.discard();
                StructureEventData.get((ServerLevel) level()).setEventComplete();
            }
            return;
        }

        if (hasLaunched) return;

        // hover: find ground beneath and float HOVER_HEIGHT above it
        BlockPos below = this.blockPosition();
        int groundY = below.getY();
        for (int i = 0; i < 10; i++) {
            BlockPos check = new BlockPos(below.getX(), groundY - i, below.getZ());
            if (!level().getBlockState(check).isAir()) {
                groundY = check.getY() + 1;
                break;
            }
        }
        double targetY = groundY + HOVER_HEIGHT;
        double currentY = this.getY();
        double newY = currentY + Math.signum(targetY - currentY) * RISE_SPEED;
        if (Math.abs(targetY - currentY) < RISE_SPEED) newY = targetY;
        this.setPos(this.getX(), newY, this.getZ());

        // find nearest player
        Player nearest = level().getNearestPlayer(this, 32);
        if (nearest == null) return;

        // once seen, mark godSpawned so the respawn loop in God.java stops,
        // then start 1-second countdown to launch
        if (!hasBeenSeen && isPlayerLookingAtMe(nearest)) {
            hasBeenSeen = true;
            launchCountdown = 20;
            // signal the respawn loop to stop
            StructureEventData.get((ServerLevel) level()).setGodSpawned();
        }

        if (hasBeenSeen && launchCountdown > 0) {
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
        return lookVec.dot(toGod) > 0.97;
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