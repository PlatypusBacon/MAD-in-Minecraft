package bunger.group.alex.wizardry.items.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;

import bunger.group.alex.wizardry.ParticleHelpers;


public class FireSpells {

    public static final ScrollItem FIRE_IGNITION = new ScrollItem(new Item.Properties()) {
        @Override
        protected void cast(Level level, Player player, ItemStack stack) {
            // Set fire to block or player aimed at within 25 blocks

            double range = 25.0;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(range));

            BlockHitResult blockHit = level.clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            if (blockHit.getType() != HitResult.Type.MISS) {
                end = blockHit.getLocation();
                BlockPos pos = blockHit.getBlockPos();

                // set block on fire
                BlockPos firePos = pos.relative(blockHit.getDirection());
                if (level.isEmptyBlock(firePos)) {
                    level.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
                }
            }

            // Entity hit detection
            AABB box = new AABB(start, end).inflate(1.0);

            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    box,
                    entity -> entity instanceof LivingEntity && entity != player
            );

            if (entityHit != null) {
                Entity entity = entityHit.getEntity();

                entity.setSecondsOnFire(6);
                end = entityHit.getLocation();
            }

            ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.FLAME);
        }
    };

    public static void create_item(String name, ScrollItem spell) {
        Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", name),
            spell);
    }

    public static void register() {
        create_item("fire_ignition", FIRE_IGNITION);
    }
}
