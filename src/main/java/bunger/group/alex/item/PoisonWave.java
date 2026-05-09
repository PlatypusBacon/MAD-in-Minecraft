package bunger.group.alex.item;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static bunger.group.alex.ParticleHelpers.spawnParticle;


public class PoisonWave extends SpellTemplate {

    private int PERPCOUNT = 0;

    public PoisonWave(Properties properties) {
        super(properties, 10, 25,  SpellTypes.POISON);
    }

    // Sends wave of poison out from usser
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

        dir = dir.multiply(1, 0, 1).normalize();


        // perp
        Vec3 perp = new Vec3(-dir.z, 0, dir.x);

        for (int j = 2; j < this.RANGE; j++) {
            Vec3 rangeVec = start.add(dir.scale(j)); // go forward i blocks
            for (double i = -15; i <= 15; i++) {
                Vec3 points = rangeVec.add(perp.scale(i/5));
                particle_pillar(level, points, j, user);
            }
        }
    }

    private void particle_pillar(Level level, Vec3 pos, int delay, LivingEntity user)
    {
        // Cast to floor
        int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, (int) pos.x, (int) pos.z);
        Vec3 newPos = new Vec3(pos.x, y, pos.z);

        delay *= 3;
        // Particles
        SpellHelpers.runAfterTicks(delay, () -> spawnParticle(level, newPos, ParticleTypes.SNEEZE));
        SpellHelpers.runAfterTicks(delay+4, () -> spawnParticle(level, newPos.add(0, 0.75, 0), ParticleTypes.SNEEZE));
        SpellHelpers.runAfterTicks(delay+8, () -> spawnParticle(level, newPos.add(0, 1.5, 0), ParticleTypes.SNEEZE));
        SpellHelpers.runAfterTicks(delay+12, () -> spawnParticle(level, newPos.add(0, 2.25, 0), ParticleTypes.SNEEZE));

        // poison and hurt
        PERPCOUNT++;
        if (PERPCOUNT % 5 == 0) { // Delays it to every 5 sideways, should still be fine
            SpellHelpers.runAfterTicks(delay, () -> {
                AABB searchBox = new AABB(
                        newPos.x - 0.5, newPos.y, newPos.z - 0.5,
                        newPos.x + 0.5, newPos.y + 3, newPos.z + 0.5
                );
                List<LivingEntity> nearby = level.getEntitiesOfClass(
                        LivingEntity.class,
                        searchBox,
                        e -> true
                );

                for (LivingEntity entity : nearby) {
                    entity.addEffect(new MobEffectInstance(
                            MobEffects.POISON,
                            200,
                            2
                    ), entity);
                    DamageSource source = level.damageSources().indirectMagic(entity, user);
                    entity.hurtServer((ServerLevel) level, source, 0.5f);
                }
            });
        }
    }
}
