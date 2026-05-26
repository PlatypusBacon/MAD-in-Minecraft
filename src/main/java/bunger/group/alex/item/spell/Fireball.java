package bunger.group.alex.item.spell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static bunger.group.alex.ParticleHelpers.spawnVolatileParticle;

public class Fireball extends SpellTemplate {
    public Fireball(Properties properties) {
        super(properties.useCooldown(5.0f), 70, 40, SpellTypes.FIRE);
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

        advanceBeam(level, user, dir, start, 1, 1, 2, ParticleTypes.FLAME);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
            explode(level, user, blockHit.getBlockPos().getCenter());
        }
        if (entityHit != null) {
            explode(level, user, entityHit.position());
        }
    }

    public void explode(Level level, LivingEntity user, Vec3 pos)
    {
        for (int i = 0; i < 50; i++){
            spawnVolatileParticle(level, pos, ParticleTypes.LARGE_SMOKE);
            spawnVolatileParticle(level, pos, ParticleTypes.FLAME);
            spawnVolatileParticle(level, pos, ParticleTypes.LAVA);
        }

        // Boom
        level.explode(
                user,
                pos.x, pos.y+.5, pos.z,
                5.0F,
                true,
                Level.ExplosionInteraction.TNT
        );
    }
}

