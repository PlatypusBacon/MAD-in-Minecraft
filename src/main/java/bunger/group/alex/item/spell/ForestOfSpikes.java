package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;

public class ForestOfSpikes extends SpellTemplate {
    public ForestOfSpikes(Properties properties) {
        super(properties.useCooldown(10.0f), 110, 50, SpellTypes.EARTH);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return; // I lowkey dont fuck with client only magic
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
            double radius = 15;
            Vec3 finalCentre = blockHit.getLocation();
            ParticleHelpers.runForTicks(200, new Runnable() {
                double offset = 0;
                @Override
                public void run() {
                    offset += 0.1;
                    ParticleHelpers.spawnRingParticles(level, finalCentre, radius,
                            offset, ParticleTypes.DUST_PLUME);
                    offset += 0.1;
                    ParticleHelpers.spawnRingParticles(level, finalCentre, radius,
                            offset, ParticleTypes.DUST_PLUME);
                }
            });

            int[] tickCounter = {0};

            SpellHelpers.runForTicks(200, () -> {
                tickCounter[0]++;
                if (tickCounter[0] % 2 == 0) {
                    double angle = Math.random() * Math.PI * 2;
                    double dist = Math.sqrt(Math.random()) * radius;

                    double offsetX = Math.cos(angle) * dist;
                    double offsetZ = Math.sin(angle) * dist;

                    int x = (int) (finalCentre.x + offsetX);
                    int z = (int) (finalCentre.z + offsetZ);
                    int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z) -1;

                    BlockPos place = new BlockPos(x, y, z);

                    if (level.getBlockState(place).is(Blocks.POINTED_DRIPSTONE)) {
                        return;
                    }

                    int height = java.util.concurrent.ThreadLocalRandom.current().nextInt(2, 8);

                    List<BlockPos> positions = new ArrayList<>();

                    for (int i = 0; i < height; i++) {
                        place = place.above();
                        positions.add(place);

                        if (level.isEmptyBlock(place) ) {
                            AABB box = new AABB(place);
                            List<Entity> entities = level.getEntities(null, box);

                            for (Entity e : entities) {
                                e.teleportRelative(0, 1.1, 0);
                                e.setDeltaMovement(e.getDeltaMovement().add(0, 0.12, 0));
                                e.hurtMarked = true;
                            }

                            level.setBlock(place, Blocks.POINTED_DRIPSTONE.defaultBlockState(), 3);
                        }
                    }
                    place = place.above();
                    // now throw down
                    AABB box = new AABB(place);
                    List<Entity> entities = level.getEntities(null, box);
                    for (Entity e : entities) {
                        e.setDeltaMovement(e.getDeltaMovement().x, -1, e.getDeltaMovement().z);

                        e.fallDistance = height;
                        e.hurtMarked = true;
                    }

                    // Now queue removal

                    final Level finalLevel = level;
                    final List<BlockPos> topToBottom = positions.reversed();

                    for (int j = 0; j < topToBottom.size(); j++) {
                        int ticks = 300 + 100 * j; // every 5 seconds remove a spike and only after spwaning ends
                        BlockPos pos = topToBottom.get(j);
                        SpellHelpers.runAfterTicks(ticks, () -> {
                            if (finalLevel.getBlockState(pos).is(Blocks.POINTED_DRIPSTONE)) {
                                finalLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        });
                    }

                }

            });
        }

    }

}
