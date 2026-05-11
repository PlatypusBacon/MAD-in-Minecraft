package bunger.group.alex.item.spell;

import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static bunger.group.alex.ParticleHelpers.spawnStaticParticle;
import static bunger.group.alex.ParticleHelpers.spawnVolatileParticle;
import static net.minecraft.world.phys.Vec3.atCenterOf;


public class PoisonRain extends SpellTemplate {

    public PoisonRain(Properties properties) {
        super(properties, 10, 25,  SpellTypes.POISON);
    }

    // Sends wave of poison out from user
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

        advanceBeam(level, user, dir, start, 1, 1, 1, ParticleTypes.SNEEZE);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
        }
        if (entityHit != null) {
        }
    }
}
