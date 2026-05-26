package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SummonWater extends SpellTemplate {

    public SummonWater(Properties properties) {
        super(properties, 20, 30, SpellTypes.WATER);
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

        advanceBeam(level, user, dir, start, 1, 4, 1, ParticleTypes.SPLASH);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
            BlockPos pos = blockHit.getBlockPos().relative(blockHit.getDirection());

            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            }
        }
        if (entityHit != null) {
            BlockPos pos = BlockPos.containing(entityHit.position());
            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            }
        }
    }
}
