package bunger.group.alex.item;

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
            end = entityHit.getLocation();
            BlockPos pos = BlockPos.containing(entity.position());
            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            }

        } else if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
            Vec3 hitPoint = blockHit.getLocation();
            BlockPos pos = new BlockPos(
                    (int) hitPoint.x,
                    (int) hitPoint.y,
                    (int) hitPoint.z
            );
            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).canBeReplaced()) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            }
        }

        ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.SPLASH);
    }
}
