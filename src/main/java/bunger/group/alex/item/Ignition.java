package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import java.util.Optional;

public class Ignition extends SpellTemplate {
    public Ignition(Properties properties) {
        super(properties, 20, 25, SpellTypes.FIRE);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return; // I lowkey dont fuck with client only magic
        }

        double range = this.RANGE;

        Vec3 start = user.getEyePosition();
        Vec3 look = user.getLookAngle();
        Vec3 end = start.add(look.scale(range));

        BlockHitResult blockHit = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                user
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
            BlockPos center = blockHit.getBlockPos();

            int radius = 2; // adjust for spread size
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos nearby = center.offset(x, y, z);

                        // try to place fire on top, sides, and bottom of each block
                        for (Direction dir : Direction.values()) {
                            BlockPos firePos = nearby.relative(dir);
                            if (!level.isEmptyBlock(nearby) && level.isEmptyBlock(firePos)) {
                                level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }


        ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.FLAME);
    }
}

