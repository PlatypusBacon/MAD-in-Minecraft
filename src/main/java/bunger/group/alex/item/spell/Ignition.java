package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.minecraft.world.phys.Vec3.atCenterOf;

public class Ignition extends SpellTemplate {
    public Ignition(Properties properties) {
        super(properties, 20, 25, SpellTypes.FIRE);
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

        advanceBeam(level, user, dir, start, 1, 2, 1, ParticleTypes.FLAME);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
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
        if (entityHit != null) {
            entityHit.setRemainingFireTicks(60);
            DamageSource source = level.damageSources().indirectMagic(entityHit, user);
            entityHit.hurtServer((ServerLevel) level, source, 0f); // ensure kill

            // light blocks beneath them
            BlockPos below = BlockPos.containing(entityHit.position()).below();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = below.offset(x, 0, z);
                    BlockPos firePos = pos.above();
                    if (!level.isEmptyBlock(pos) && level.isEmptyBlock(firePos)) {
                        level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}

