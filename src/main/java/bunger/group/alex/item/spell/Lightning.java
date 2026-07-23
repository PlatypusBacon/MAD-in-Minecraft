package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class Lightning extends SpellTemplate {
    public Lightning(Properties properties) {
        super(properties.useCooldown(1.0f), 30, 25, SpellTypes.LIGHTNING);
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

        advanceBeam(level, user, dir, start, 1, 3, 1, ParticleTypes.CLOUD);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
            BlockPos pos = blockHit.getBlockPos();
            Vec3 centre = Vec3.atCenterOf(pos).add(0, 0.6, 0);

            int x = (int) centre.x;
            int z = (int) centre.z;
            int y = level.getHeight(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    x,
                    z
            );

            BlockPos place = new BlockPos(x, y, z);

            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightningBolt.setPos(place.getCenter());
            level.addFreshEntity(lightningBolt);

            AABB area = new AABB(place);
            List<Entity> entities = level.getEntities((Entity) null, area, Entity::isAlive);
            for (Entity hEntity : entities) {
                DamageSource source = level.damageSources().indirectMagic(user, user);
                hEntity.hurtServer((ServerLevel) level, source, 0.0f);
            }
        }
        if (entityHit != null) {
            DamageSource source = level.damageSources().indirectMagic(entityHit, user);
            entityHit.hurtServer((ServerLevel) level, source, 0.0f);

            Vec3 centre = entityHit.position().add(0, 0.1, 0);

            int x = (int) centre.x;
            int z = (int) centre.z;
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);

            BlockPos place = new BlockPos(x, y, z);

            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightningBolt.setPos(place.getCenter());
            level.addFreshEntity(lightningBolt);

        }
    }


    public void cst(Level level, LivingEntity user, ItemStack stack) {
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

        Vec3 centre = null;
        if (blockHit.getType() != HitResult.Type.MISS) {
            BlockPos pos = blockHit.getBlockPos();
            centre = Vec3.atCenterOf(pos).add(0, 0.6, 0);
        }
        if (entityHit != null) {
            Entity entity = entityHit.getEntity();
            centre = entity.position().add(0, 0.1, 0);
        }

        if (centre != null) {
            int x = (int) centre.x;
            int z = (int) centre.z;
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);

            BlockPos place = new BlockPos(x, y, z);

            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            lightningBolt.setPos(place.getCenter());
            level.addFreshEntity(lightningBolt);
        }

        ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.ELECTRIC_SPARK);
    }
}

