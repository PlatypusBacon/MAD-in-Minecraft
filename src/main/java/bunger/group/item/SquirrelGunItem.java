package bunger.group.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SquirrelGunItem extends Item {

    private static final int COOLDOWN_TICKS = 6; // fast fire (0.3s)

    public SquirrelGunItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {

            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.pass(stack);
            }

            // TODO: check ammo here later

            shoot(world, player);

            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.success(stack);
    }

    private void shoot(Level world, Player player) {
        // TODO: spawn projectile entity
        System.out.println("Bang!");
    }
}