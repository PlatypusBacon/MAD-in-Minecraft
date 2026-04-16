package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.Optional;

public class AgarthanThunder extends Item {

    public AgarthanThunder(Properties properties) {
        super(properties);
    }

    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return; // I lowkey dont fuck with client only magic
        }
        double range = 40.0;
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
            Vec3 finalCentre = centre;
            ParticleHelpers.runForTicks(200, new Runnable() {
                double offset = 0;
                @Override
                public void run() {
                    offset += 0.1;
                    ParticleHelpers.spawnRingParticles(level, finalCentre, 20,
                            offset, ParticleTypes.ELECTRIC_SPARK);
                }
            });
        }

        if (centre != null) {
            double radius = 20.0;
            Vec3 finalCentre1 = centre;
            SpellHelpers.runForTicks(200, () -> {
                for (int i = 0; i < 5; i++) {
                    double angle = Math.random() * Math.PI * 2;
                    double dist = Math.sqrt(Math.random()) * radius;

                    double offsetX = Math.cos(angle) * dist;
                    double offsetZ = Math.sin(angle) * dist;

                    BlockPos place = new BlockPos((int) (finalCentre1.x + offsetX),
                            (int) finalCentre1.y,
                            (int) (finalCentre1.z + offsetZ));

                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    lightningBolt.setPos(place.getCenter());
                    level.addFreshEntity(lightningBolt);
                }

            });
        }
    }


    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        ItemStack stack = user.getItemInHand(hand);
        this.cast(level, user, stack);

        return InteractionResult.SUCCESS;
    }
}
