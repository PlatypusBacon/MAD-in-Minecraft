package bunger.group.alex.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class Longbow extends ProjectileWeaponItem {
    public static final int MAX_DRAW_DURATION = 40;
    public static final int DEFAULT_RANGE = 30;
    public static final int ARROW_DAMAGE = 2;
    public static final float POWER_MULT = 4.5F;

    public Longbow(final Item.Properties properties) {
        super(properties);
    }

    public boolean releaseUsing(final @NonNull ItemStack itemStack, final @NonNull Level level, final @NonNull LivingEntity entity, final int remainingTime) {
        if (!(entity instanceof Player player)) {
            return false;
        } else {
            ItemStack projectile = player.getProjectile(itemStack);
            if (projectile.isEmpty()) {
                return false;
            } else {
                int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
                float pow = getPowerForTime(timeHeld);
                if ((double)pow < 0.1) {
                    return false;
                } else {
                    List<ItemStack> firedProjectiles = draw(itemStack, projectile, player);
                    if (level instanceof ServerLevel serverLevel) {
                        if (!firedProjectiles.isEmpty()) {
                            this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, firedProjectiles,
                                    pow * POWER_MULT, 0.75F, pow == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Entity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return true;
                }
            }
        }
    }

    public void shootProjectile(final @NonNull LivingEntity shooter, final Projectile projectileEntity, final int index, final float power, final float uncertainty, final float angle, final @Nullable LivingEntity targetOverrride) {
        projectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, power, uncertainty);
        if (projectileEntity instanceof AbstractArrow arrow) {
            arrow.setBaseDamage(ARROW_DAMAGE);
        }
    }

    public static float getPowerForTime(final int timeHeld) {
        float pow = (float)timeHeld / MAX_DRAW_DURATION;
        pow = (pow * pow + pow * 2.0F) / 3.0F;
        if (pow > 1.0F) {
            pow = 1.0F;
        }

        return pow;
    }

    public int getUseDuration(final @NonNull ItemStack itemStack, final @NonNull LivingEntity user) {
        return 72000;
    }

    public @NonNull ItemUseAnimation getUseAnimation(final @NonNull ItemStack itemStack) {
        return ItemUseAnimation.BOW;
    }

    public @NonNull InteractionResult use(final @NonNull Level level, final Player player, final @NonNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        boolean foundProjectile = !player.getProjectile(itemStack).isEmpty();
        if (!player.hasInfiniteMaterials() && !foundProjectile) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    public @NonNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return DEFAULT_RANGE;
    }
}