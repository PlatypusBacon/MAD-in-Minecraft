package bunger.group.alex.item.spell;

import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static bunger.group.alex.ParticleHelpers.spawnVolatileParticle;


public class Tsunami extends SpellTemplate {

    private int PERPCOUNT = 0;

    public Tsunami(Properties properties) {
        super(properties, 50, 30,  SpellTypes.WATER);
    }

    // Sends wave of poison out from usser
    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }

        Vec3 start = user.getEyePosition();
        Vec3 target = getCastTarget(user);
        Vec3 dir;
        if (target != null) {
            dir = target.subtract(start).normalize();
        } else {
            dir = user.getLookAngle();
        }

        dir = dir.multiply(1, 0, 1).normalize();


        // perp
        Vec3 perp = new Vec3(-dir.z, 0, dir.x);

        for (int j = 2; j < this.RANGE; j++) {
            Vec3 rangeVec = start.add(dir.scale(j)); // go forward i blocks
            for (double i = -50; i <= 50; i++) {
                Vec3 points = rangeVec.add(perp.scale(i/10));
                particle_pillar(level, points, j, user, dir);
            }
        }
    }

    private void particle_pillar(Level level, Vec3 pos, int delay, LivingEntity user, Vec3 dir)
    {
        // Cast to floor below eyes
        Vec3 rayStart = new Vec3(pos.x, pos.y+1, pos.z);
        Vec3 rayEnd = new Vec3(pos.x, pos.y - 15, pos.z);
        BlockHitResult hit = level.clip(new ClipContext(rayStart, rayEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, user));
        double y = (hit.getType() == HitResult.Type.BLOCK) ? hit.getLocation().y : pos.y;
        Vec3 newPos = new Vec3(pos.x, y, pos.z);


        delay *= 3;
        // Particles
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            SpellHelpers.runAfterTicks(delay, () -> spawnVolatileParticle(level, newPos.add(dir.scale(0.5* finalI)), ParticleTypes.SPLASH));
            SpellHelpers.runAfterTicks(delay + 3, () -> spawnVolatileParticle(level, newPos.add(0, 0.75, 0).add(dir.scale(0.5* finalI)), ParticleTypes.FALLING_WATER));
            SpellHelpers.runAfterTicks(delay + 6, () -> spawnVolatileParticle(level, newPos.add(0, 1.5, 0).add(dir.scale(0.5* finalI)), ParticleTypes.FALLING_WATER));
            SpellHelpers.runAfterTicks(delay + 8, () -> spawnVolatileParticle(level, newPos.add(0, 2.25, 0).add(dir.scale(0.5* finalI)), ParticleTypes.FALLING_WATER));
            SpellHelpers.runAfterTicks(delay + 10, () -> spawnVolatileParticle(level, newPos.add(0, 2.5, 0).add(dir.scale(0.5* finalI)), ParticleTypes.SPLASH));
        }
        // hurt and push back
        PERPCOUNT++;
        if (PERPCOUNT % 10 == 0) { // Delays it to every 10 sideways, should still be fine
            SpellHelpers.runAfterTicks(delay, () -> {
                AABB searchBox = new AABB(
                        newPos.x - 0.5, newPos.y, newPos.z - 0.5,
                        newPos.x + 0.5, newPos.y + 3, newPos.z + 0.5
                );
                List<LivingEntity> nearby = level.getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        e -> true
                );

                for (LivingEntity entity : nearby) {
                    if (!entity.equals(user)) {

                        DamageSource source = level.damageSources().indirectMagic(entity, user);
                        entity.hurtServer((ServerLevel) level, source, 1.0f);

                        Vec3 knockbackDir = entity.position().subtract(user.position()).normalize();
                        entity.knockback(0.6, -knockbackDir.x, -knockbackDir.z);
                    }
                }
            });
        }
    }
}
