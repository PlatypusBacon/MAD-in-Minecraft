package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Zap extends SpellTemplate {
    public Zap(Properties properties) {
        super(properties.useCooldown(0.1f), 5, 15, SpellTypes.LIGHTNING);
    }


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

        advanceBeam(level, user, dir, start, 1, 4, 1, ParticleTypes.FIREWORK);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
            return; // Doesnt do anything
        }
        if (entityHit != null) {
            DamageSource source = level.damageSources().indirectMagic(entityHit, user);
            entityHit.hurtServer((ServerLevel) level, source, 1.5f);

            // Manual knockback in the direction of the beam
            Vec3 knockbackDir = entityHit.position().subtract(user.position()).normalize();
            entityHit.knockback(0.2, -knockbackDir.x, -knockbackDir.z);

            if (entityHit instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 10, 4, false, false));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 10; // ticks (20 ticks = 1 second)
    }
}
