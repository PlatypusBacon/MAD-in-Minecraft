package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.WeatherData;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChannelStorm extends SpellTemplate {
    public ChannelStorm(Properties properties) {
        super(properties.useCooldown(3.0f), 80, 40, SpellTypes.LIGHTNING);
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

        advanceBeam(level, user, dir, start, 1, 1, 1, ParticleTypes.CLOUD);
    }

    @Override
    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        Vec3 centre = null;
        if (blockHit != null) {
            BlockPos pos = blockHit.getBlockPos();
            centre = Vec3.atCenterOf(pos).add(0, 0.6, 0);
        }
        if (entityHit != null) {
            centre = entityHit.position().add(0, 0.1, 0);
        }

        if (centre != null) {
            if (level instanceof ServerLevel serverLevel) {
                WeatherData weather = serverLevel.getWeatherData();
                weather.setRaining(true);
                weather.setRainTime(100);
                weather.setThundering(true);
                weather.setThunderTime(100);
                weather.setClearWeatherTime(0);
            }

            Vec3 finalCentre = centre;
            ParticleHelpers.runForTicks(120, new Runnable() {
                double offset = 0;
                @Override
                public void run() {
                    offset += 0.1;
                    ParticleHelpers.spawnRingParticles(level, finalCentre, 3,
                            offset, ParticleTypes.ELECTRIC_SPARK);
                }
            });
        }

        if (centre != null) {
            double radius = 3;
            Vec3 finalCentre1 = centre;
            int[] tickCounter = {0};
            SpellHelpers.runForTicks(100, () -> {
                tickCounter[0]++;
                if (tickCounter[0] % 10 == 0) {
                    double angle = Math.random() * Math.PI * 2;
                    double dist = Math.sqrt(Math.random()) * radius;

                    double offsetX = Math.cos(angle) * dist;
                    double offsetZ = Math.sin(angle) * dist;

                    int x = (int) (finalCentre1.x + offsetX);
                    int z = (int) (finalCentre1.z + offsetZ);
                    int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);

                    BlockPos place = new BlockPos(x, y, z);

                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                    lightningBolt.setPos(place.getCenter());
                    level.addFreshEntity(lightningBolt);
                }
            });
        }
    }
}
