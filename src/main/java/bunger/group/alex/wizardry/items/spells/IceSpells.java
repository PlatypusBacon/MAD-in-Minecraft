package bunger.group.alex.wizardry.items.spells;

import bunger.group.alex.wizardry.ParticleHelpers;
import bunger.group.alex.wizardry.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public class IceSpells {

    static final ScrollItem ICE_SHIELD = new ScrollItem(new Item.Properties()) {
        @Override
        public void cast(Level level, LivingEntity player, ItemStack stack) {
            Vec3 center = player.position().add(0, 1.0, 0);
            double radius = 4.0;

            Vec3 start = player.getEyePosition();
            Vec3 dir = player.getLookAngle().normalize().reverse();
            Vec3 oc = start.subtract(center);

            double a = dir.dot(dir);
            double b = 2.0 * oc.dot(dir);
            double c = oc.dot(oc) - radius * radius;

            double discriminant = b * b - 4 * a * c;
            if (discriminant < 0) return;

            double t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            Vec3 hitPoint = start.add(dir.scale(t));

            Vec3 normal = hitPoint.subtract(center).normalize();

            Vec3 up = new Vec3(0, 1, 0);
            if (Math.abs(normal.y) > 0.9) {
                up = new Vec3(1, 0, 0);
            }

            Vec3 right = normal.cross(up).normalize();
            Vec3 forward = right.cross(normal).normalize();


            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {

                    Vec3 point = hitPoint
                            .add(right.scale(x))
                            .add(forward.scale(y));

                    Vec3 snapped = center.add(
                            point.subtract(center).normalize().scale(radius)
                    );

                    BlockPos pos = new BlockPos(
                            (int) Math.floor(snapped.x),
                            (int) Math.floor(snapped.y),
                            (int) Math.floor(snapped.z)
                    );

                    if (level.isEmptyBlock(pos)) {
                        level.setBlock(pos, Blocks.ICE.defaultBlockState(), 3);

                        final BlockPos finalPos = pos;
                        final Level finalLevel = level;

                        SpellHelpers.runAfterTicks(60, () -> {
                            if (finalLevel.getBlockState(finalPos).is(Blocks.ICE) || finalLevel.getBlockState(finalPos).is(Blocks.WATER)) {
                                    finalLevel.setBlock(finalPos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        });
                    }
                }
            }
        }
    };

    public static void create_item(String name, ScrollItem spell) {
        Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", name),
            spell);
    }

    public static void register() {
        create_item("spell_ice_ice_shield", ICE_SHIELD);
    }

}
