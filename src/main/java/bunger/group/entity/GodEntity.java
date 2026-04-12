package bunger.group.entity;

import bunger.group.data.StructureEventData;
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
    private int lookTicks = 0;
    private int despawnTicks = -1;

    public GodEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.setSilent(true);
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide()) return;

        // handle despawn countdown after launch
        if (despawnTicks >= 0) {
            despawnTicks--;
            if (despawnTicks <= 0) {
                this.discard();
                StructureEventData.get((ServerLevel) level).setEventComplete();
            }
            return;
        }

        if (hasLaunched) return;

        // find nearest player
        Player nearest = level.getNearestPlayer(this, 32);
        if (nearest == null) return;

        // check if player is looking at GOD
        if (isPlayerLookingAtMe(nearest)) {
            lookTicks++;
            if (lookTicks >= 10) { // looked for 0.5 seconds
                launchPlayer(nearest);
            }
        } else {
            lookTicks = 0;
        }
    }

    private boolean isPlayerLookingAtMe(Player player) {
        Vec3 toGod = this.position()
                .subtract(player.getEyePosition()).normalize();
        Vec3 lookVec = player.getLookAngle();
        // dot product > 0.97 means within ~14 degrees
        return lookVec.dot(toGod) > 0.97;
    }

    private void launchPlayer(Player player) {
        hasLaunched = true;

        // extreme upward + forward launch
        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(
                look.x * 3.0,
                4.5,           // extreme upward force
                look.z * 3.0
        );
        player.hurtMarked = true;

        // start despawn countdown (3 seconds)
        despawnTicks = 60;
    }

    @Override
    protected void registerGoals() {
        // no AI — GOD just stands there
    }
}