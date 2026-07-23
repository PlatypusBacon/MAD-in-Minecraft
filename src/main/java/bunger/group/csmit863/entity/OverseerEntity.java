package bunger.group.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.CustomSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class OverseerEntity extends Monster {
    private int fireballCooldown = 100;
    private final ServerBossEvent bossBar = new ServerBossEvent(
            this.uuid,
            Component.literal("The Overseer"),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.PROGRESS
    );

    public OverseerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.setInvisible(true);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.FOLLOW_RANGE, 256.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0); // stop vanilla AI moving it
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossBar.removePlayer(player);
    }

    @Override
    public void playHurtSound(DamageSource source) {
        this.playSound(CustomSounds.OVERSEER_HELLO, 0.3F, 0.9F);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.playSound(CustomSounds.OVERSEER_HELLO, 0.3F, 0.9F);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) return;

        this.getNavigation().stop();

        // Shoot fireball at nearest player
        fireballCooldown--;
        if (fireballCooldown <= 0) {
            fireballCooldown = 100 + this.random.nextInt(100);

            var target = this.level().getNearestPlayer(this, 128);
            if (target != null && !target.isCreative()) {
                Vec3 dir = target.position().add(0, 1, 0)
                        .subtract(this.position()).normalize();

                LargeFireball fireball = new LargeFireball(
                        this.level(),
                        this,
                        dir,
                        8
                );
                fireball.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(fireball);
                this.playSound(bunger.group.csmit863.CustomSounds.OVERSEER_HELLO);

            }
        }
    }
}