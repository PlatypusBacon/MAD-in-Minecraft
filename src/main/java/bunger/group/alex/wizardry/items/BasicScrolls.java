package bunger.group.alex.wizardry.items;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.ClipContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.player.Player;

public class BasicScrolls {

    public static final Item BASIC_SCROLL = Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", "basic_scroll"),
            new ScrollItem(new Item.Properties())
    );

    public static final Item BASIC_SCROLL_FIRE = Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", "basic_scroll_fire"),
            new ScrollItem(new Item.Properties()) {

                @Override
                protected void cast(Level level, Player player, ItemStack stack) {

                    double reach = 20.0;

                    Vec3 start = player.getEyePosition();
                    Vec3 look = player.getLookAngle();
                    Vec3 end = start.add(look.scale(reach));

                    // Ray trace for blocks
                    BlockHitResult blockHit = level.clip(new ClipContext(
                            start,
                            end,
                            ClipContext.Block.OUTLINE,
                            ClipContext.Fluid.NONE,
                            player
                    ));

                    Vec3 hitPos = end;

                    if (blockHit.getType() != HitResult.Type.MISS) {
                        hitPos = blockHit.getLocation();

                        BlockPos pos = blockHit.getBlockPos();

                        // set block on fire (if air above solid block)
                        BlockPos firePos = pos.relative(blockHit.getDirection());

                        if (level.isEmptyBlock(firePos)) {
                            level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                        }
                    }

                    // Entity hit detection
                    AABB box = new AABB(start, hitPos).inflate(1.0);

                    EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                            level,
                            player,
                            start,
                            hitPos,
                            box,
                            entity -> entity instanceof LivingEntity && entity != player
                    );

                    if (entityHit != null) {
                        Entity entity = entityHit.getEntity();

                        entity.setSecondsOnFire(5);
                        hitPos = entityHit.getLocation();
                    }

                    // 🔥 Fire particles along the beam
                    spawnBeamParticles(level, start, hitPos);
                }
            }
    );

    static private void spawnBeamParticles(Level level, Vec3 start, Vec3 end) {

    Vec3 direction = end.subtract(start);
    double distance = direction.length();

    direction = direction.normalize();

    for (double i = 0; i < distance; i += 0.3) {

        Vec3 pos = start.add(direction.scale(i));

        ((ServerLevel) level).sendParticles(
                ParticleTypes.FLAME,
                pos.x, pos.y, pos.z,
                1,
                0, 0, 0,
                0.0
        );
    }
}

    public static void register() {}
}