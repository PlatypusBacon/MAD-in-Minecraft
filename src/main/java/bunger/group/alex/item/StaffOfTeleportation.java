package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import java.util.Optional;
import java.util.Random;

public class StaffOfTeleportation extends SpellTemplate {
    public StaffOfTeleportation(Properties properties) {
        super(properties, 50, 30, SpellTypes.STAFF);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }

        boolean selfTeleport = false;
        if (user instanceof Player player) {
            if (player.isCrouching()) {selfTeleport = true;}
        }

        double range = this.RANGE;
        Vec3 start = user.getEyePosition();
        Vec3 look = user.getLookAngle();
        Vec3 end = start.add(look.scale(range));

        EntityHitResult entityHit;
        if (!selfTeleport) {
            // Teleport someone else

            entityHit = null;
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
        } else {
            entityHit = new EntityHitResult(user);
        }

        if (entityHit != null) {
            Entity entity = entityHit.getEntity();
            end = entityHit.getLocation();

            // Spawn rings
            Vec3 centre = entity.position();

            for (int i = 0; i < 20; i++){

                final Double yOffset = (double) i * 0.1F;
                Vec3 offsetCentre = centre.add(0, yOffset, 0);

                ParticleHelpers.runForTicks(30, new Runnable() {
                    double offset = 0;
                    @Override
                    public void run() {
                        offset += 0.1;
                        ParticleHelpers.spawnRingParticles(level, offsetCentre, 1,
                                offset, ParticleTypes.SQUID_INK);
                    }
                });
            }

            double angle = Math.random() * Math.PI * 2;
            double dist = Math.sqrt(Math.random()) * 100; // teleports to random place within 100 blocks

            double offsetX = Math.cos(angle) * dist;
            double offsetZ = Math.sin(angle) * dist;

            int x = (int) (centre.x + offsetX);
            int z = (int) (centre.z + offsetZ);
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);

            entity.teleportTo(x, y, z);

            // now spawn particles around fella AFTER

            Vec3 newCentre = new Vec3(x, y, z);

            for (int i = 0; i < 20; i++){

                final Double yOffset = (double) i * 0.1F;
                Vec3 offsetCentre = newCentre.add(0, yOffset, 0);

                ParticleHelpers.runForTicks(30, new Runnable() {
                    double offset = 0;
                    @Override
                    public void run() {
                        offset += 0.1;
                        ParticleHelpers.spawnRingParticles(level, offsetCentre, 1,
                                offset, ParticleTypes.SQUID_INK);
                    }
                });
            }
        }
        if (!selfTeleport) {
            ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.SQUID_INK);
        }
    }
}

