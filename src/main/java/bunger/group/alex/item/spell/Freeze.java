package bunger.group.alex.item.spell;

import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import static bunger.group.alex.ParticleHelpers.spawnVolatileParticle;

public class Freeze extends SpellTemplate{

    private static final int r = 1;

    public Freeze(Properties properties) {
        super(properties, 90, 25, SpellTypes.ICE);
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

        advanceBeam(level, user, dir, start, 1, 1, 3, ParticleTypes.SNOWFLAKE);
    }


    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        BlockPos ctrBlock = null;

        if (blockHit != null) {
            ctrBlock = blockHit.getBlockPos();
        }
        if (entityHit != null) {
            ctrBlock = BlockPos.containing(entityHit.position()).above();
            DamageSource source = level.damageSources().indirectMagic(entityHit, user);
            entityHit.hurtServer((ServerLevel) level, source, 0.0f); // for kill credit

            if (entityHit instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 3, true, false));
                living.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 100, 3, true, false));
            }
        }

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z= -r; z <= r; z++) {
                    BlockPos pos = ctrBlock.offset(x, y, z);
                    if (level.isEmptyBlock(pos)) {
                        level.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);

                        final BlockPos finalPos = pos;
                        final Level finalLevel = level;

                        SpellHelpers.runAfterTicks(100, () -> {
                            if (finalLevel.getBlockState(finalPos).is(Blocks.ICE) || finalLevel.getBlockState(finalPos).is(Blocks.WATER)) {
                                finalLevel.setBlock(finalPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        });
                        spawnVolatileParticle(level, pos.getCenter(), ParticleTypes.SNOWFLAKE);
                    }
                }
            }
        }
    }
}
