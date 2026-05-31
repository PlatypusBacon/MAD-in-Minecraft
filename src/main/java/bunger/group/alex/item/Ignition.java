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
            return;
        }

        double range = this.RANGE;

        Vec3 start = user.getEyePosition();
        Vec3 end = getCastEndPoint(user);
        Vec3 look = user.getLookAngle();

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
            entity.setRemainingFireTicks(100);
            end = entityHit.getLocation();

            // light blocks beneath them
            BlockPos below = BlockPos.containing(entity.position()).below();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = below.offset(x, 0, z);
                    BlockPos firePos = pos.above();
                    if (!level.isEmptyBlock(pos) && level.isEmptyBlock(firePos)) {
                        level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        } else if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
            Vec3 hitPoint = blockHit.getLocation();
            Direction hitFace = blockHit.getDirection();

            Vec3 normal = new Vec3(hitFace.getStepX(), hitFace.getStepY(), hitFace.getStepZ());
            Vec3 up = Math.abs(normal.y) > 0.9 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
            Vec3 right = normal.cross(up).normalize();
            Vec3 forward = right.cross(normal).normalize();

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    Vec3 point = hitPoint
                            .add(right.scale(x))
                            .add(forward.scale(y));

                    BlockPos nearbyPos = BlockPos.containing(point);
                    BlockPos solidPos = nearbyPos.relative(hitFace.getOpposite());

                    if (level.getBlockState(solidPos).is(Blocks.SNOW)) {
                        level.setBlock(solidPos, Blocks.FIRE.defaultBlockState(), 3);
                    } else {
                        for (Direction dir : Direction.values()) {
                            BlockPos firePos = solidPos.relative(dir);
                            if (!level.isEmptyBlock(solidPos) && level.isEmptyBlock(firePos)) {
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

