package bunger.group.tyler2.item;

import bunger.group.tyler.sound.ModSounds;
import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler3.entity.RockProjectileEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import bunger.group.tyler2.block.ModBlocks;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class SlingItem extends BowItem {
    public static final int MAX_DRAW_DURATION = 40;
    public static final int DEFAULT_RANGE = 15;
    public SlingItem(Properties properties) {
        super(properties);
    }
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() == ModBlocks.ROCK.asItem();
    }

    public static float getPowerForTime(int timeHeld) {
        float pow = (float) timeHeld / MAX_DRAW_DURATION;
        pow = (pow * pow + pow * 2.0F) / 3.0F;
        if (pow > 1.0F) pow = 1.0F;
        return pow;
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weaponStack, ItemStack ammoStack, boolean isCrit) {
        return new RockProjectileEntity(ModEntities.ROCK_PROJECTILE, shooter, level, ammoStack);
    }
    @Override
    public boolean releaseUsing(ItemStack itemStack, net.minecraft.world.level.Level level, LivingEntity entity, int remainingTime) {
        if (!(entity instanceof net.minecraft.world.entity.player.Player player)) return false;

        ItemStack projectile = player.getProjectile(itemStack);
        if (projectile.isEmpty()) return false;

        int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
        float pow = getPowerForTime(timeHeld);
        if ((double) pow < 0.1) return false;

        var firedProjectiles = draw(itemStack, projectile, player);
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            if (!firedProjectiles.isEmpty()) {
                this.shoot(serverLevel, player, player.getUsedItemHand(), itemStack, firedProjectiles, pow * 3.0F, 1.0F, pow == 1.0F, null);
            }
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.SLING_SHOT,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + pow * 0.5F);
        player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));
        return true;
    }
}