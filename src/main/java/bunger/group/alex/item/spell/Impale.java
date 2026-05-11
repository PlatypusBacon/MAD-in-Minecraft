package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Impale extends SpellTemplate {

    public Impale(Properties properties) {
        super(properties, 20, 15,  SpellTypes.EARTH);
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

        advanceBeam(level, user, dir, start, 1, 1, 2, ParticleTypes.DUST_PLUME);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        Vec3 end = null;
        BlockPos blockTouched = null;
        if (blockHit != null) {
            end = blockHit.getLocation();
            blockTouched = blockHit.getBlockPos();
        }
        if (entityHit != null) {
            end = entityHit.position();
            blockTouched = BlockPos.containing(entityHit.position()).below();
        }
        if (blockTouched == null)
        {
            return;
        }

        if (level.getBlockState(blockTouched).is(Blocks.POINTED_DRIPSTONE)) {
            return;
        }

        int height = ThreadLocalRandom.current().nextInt(2, 6);

        List<BlockPos> positions = new ArrayList<>();

        boolean doThrow = false;
        for (int i = 0; i < height; i++) {
            if (!level.getBlockState(blockTouched).isSolid()) {
                break;
            }
            if (!doThrow) doThrow = true;

            blockTouched = blockTouched.above();
            positions.add(blockTouched);

            if (level.isEmptyBlock(blockTouched)) {
                AABB box = new AABB(blockTouched);
                List<Entity> entities = level.getEntities(null, box);

                for (Entity e : entities) {
                    e.teleportRelative(0, 1.1, 0);
                    e.setDeltaMovement(e.getDeltaMovement().add(0, 0.12, 0));
                    e.hurtMarked = true;
                }

                level.setBlock(blockTouched, Blocks.POINTED_DRIPSTONE.defaultBlockState(), 3);
            } else{
                break;
            }
        }
        blockTouched = blockTouched.above();
        // now throw down
        if (doThrow) {
            AABB box = new AABB(blockTouched);
            List<Entity> entities = level.getEntities(null, box);
            for (Entity e : entities) {
                e.setDeltaMovement(e.getDeltaMovement().x, -1, e.getDeltaMovement().z);

                e.fallDistance = height;
                e.hurtMarked = true;
                DamageSource source = level.damageSources().indirectMagic(e, user);
                e.hurtServer((ServerLevel) level, source, 0f); // ensure kill
            }
            ParticleHelpers.spawnRingParticles(level, end, 0.5, 0,  ParticleTypes.DUST_PLUME);
        }
        // Now queue removal

        final Level finalLevel = level;
        final List<BlockPos> topToBottom = positions.reversed();

        for (int i = 0; i < topToBottom.size(); i++) {
            int ticks = 100 + 100 * i; // every 5 seconds remove a spike
            BlockPos pos = topToBottom.get(i);
            SpellHelpers.runAfterTicks(ticks, () -> {
                if (finalLevel.getBlockState(pos).is(Blocks.POINTED_DRIPSTONE)) {
                    finalLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            });
        }
    }

}
