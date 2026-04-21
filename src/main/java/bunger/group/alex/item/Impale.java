package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Optional;

public class Impale extends SpellTemplate {


    public Impale(Properties properties) {
        super(properties, 20, 10,  SpellTypes.EARTH);
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

        if (blockHit.getType() != HitResult.Type.MISS) {
            end = blockHit.getLocation();
            BlockPos blockTouched = blockHit.getBlockPos();
            if (level.getBlockState(blockTouched).is(Blocks.POINTED_DRIPSTONE)) {
                return;
            }

            int height = java.util.concurrent.ThreadLocalRandom.current().nextInt(3, 7);

            for (int i = 0; i < height; i++) {
                blockTouched = blockTouched.above();

                if (level.isEmptyBlock(blockTouched)) {
                    AABB box = new AABB(blockTouched);
                    List<Entity> entities = level.getEntities(null, box);

                    for (Entity e : entities) {
                        e.teleportRelative(0, 1.1, 0);
                        e.setDeltaMovement(e.getDeltaMovement().add(0, 0.12, 0));
                        e.hurtMarked = true;
                    }

                    level.setBlock(blockTouched, Blocks.POINTED_DRIPSTONE.defaultBlockState(), 3);
                }
            }
            blockTouched = blockTouched.above();
            // now throw down
            AABB box = new AABB(blockTouched);
            List<Entity> entities = level.getEntities(null, box);
            for (Entity e : entities) {
                e.setDeltaMovement(e.getDeltaMovement().x, -1, e.getDeltaMovement().z);

                e.fallDistance = 3F;
                e.hurtMarked = true;
            }
        }

        ParticleHelpers.spawnRingParticles(level, end, 0.5, 0,  ParticleTypes.DUST_PLUME);
    }

}
