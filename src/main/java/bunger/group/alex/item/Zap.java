package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.Optional;

public class Zap extends SpellTemplate {
    public Zap(Properties properties) {
        super(properties.useCooldown(0.1f), 4, 20, SpellTypes.LIGHTNING);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }

        double range = this.RANGE;

        Vec3 start = user.getEyePosition();
        Vec3 look = user.getLookAngle();
        Vec3 end = start.add(look.scale(range));


        EntityHitResult entityHit = null;
        double closestDist = range * range;

        AABB searchBox = user.getBoundingBox()
                .expandTowards(look.scale(range))
                .inflate(1.0);

        for (Entity entity : level.getEntities(user, searchBox, e -> e instanceof LivingEntity && e != user)) {
            AABB aabb = entity.getBoundingBox().inflate(0.3);
            Optional<Vec3> hitPoint = aabb.clip(start, end);
            if (hitPoint.isPresent()) {
                double dist = start.distanceToSqr(hitPoint.get());
                if (dist < closestDist) {
                    closestDist = dist;
                    entityHit = new EntityHitResult(entity, hitPoint.get());
                }
            }
        }

        if (entityHit != null) {
            Entity entity = entityHit.getEntity();
            DamageSource source = level.damageSources().indirectMagic(entity, user);
            entity.hurtServer((ServerLevel) level, source, 1.5f);

            if (entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10, 4, false, false));
            }
            end = entityHit.getLocation();
        }
        ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.ELECTRIC_SPARK);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 10; // ticks (20 ticks = 1 second)
    }
}
