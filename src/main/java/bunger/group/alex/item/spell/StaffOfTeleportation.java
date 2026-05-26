package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffOfTeleportation extends SpellTemplate {
    public StaffOfTeleportation(Properties properties) {
        super(properties.useCooldown(1.0f), 50, 30, SpellTypes.STAFF);
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

        boolean selfTeleport = false;
        if (user instanceof Player player) {
            if (player.isCrouching()) {selfTeleport = true;}
        }

        if (!selfTeleport) {
            advanceBeam(level, user, dir, start, 1, 3, 1, ParticleTypes.SQUID_INK);
        } else {
            impactResult(level, user, null, user); // hit yourself
        }
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
            return; // Doesnt do anything
        }
        if (entityHit != null) {
            Entity entity = entityHit;

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
            int minY = level.dimensionType().minY();
            int maxY = minY + level.dimensionType().height() - 2;


            List<Integer> validYs = new ArrayList<>();

            for (int searchY = minY + 1; searchY < maxY; searchY++) {
                BlockPos floor = new BlockPos(x, searchY - 1, z);
                BlockPos feet  = new BlockPos(x, searchY,     z);
                BlockPos head  = new BlockPos(x, searchY + 1, z);

                if (level.getBlockState(floor).isSolid() &&
                        level.getBlockState(feet).isAir()    &&
                        level.getBlockState(head).isAir()) {
                    validYs.add(searchY);
                }
            }

            int y;
            if (!validYs.isEmpty()) {
                y = validYs.get((int)(Math.random() * validYs.size()));
            } else {
                y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            }

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

            // Now teleport into the particles

            entity.teleportTo(x, y, z);
        }
    }
}

