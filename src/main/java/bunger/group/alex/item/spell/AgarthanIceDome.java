package bunger.group.alex.item.spell;

import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class AgarthanIceDome extends SpellTemplate {
    public AgarthanIceDome(Properties properties) {
        super(properties.useCooldown(2.0f), 40, 7,  SpellTypes.ICE);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return; // I lowkey dont fuck with client only magic
        }
        Vec3 center = user.position().add(0, 1.0, 0);
        double radius = (double) this.RANGE;

        Vec3 start = user.getEyePosition();
        Vec3 dir = user.getLookAngle().normalize().reverse();
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

        int areaDelta = 9;

        for (int x = -areaDelta; x <= areaDelta; x++) {
            for (int y = -areaDelta; y <= areaDelta; y++) {

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

                    SpellHelpers.runAfterTicks(80, () -> {
                        if (finalLevel.getBlockState(finalPos).is(Blocks.ICE) || finalLevel.getBlockState(finalPos).is(Blocks.WATER)) {
                            finalLevel.setBlock(finalPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    });
                }
            }
        }
    }
}
