package bunger.group.alex.wizardry.items.spells;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrollItem extends Item {

    public ScrollItem(Properties properties) {
        super(properties);
    }

    protected void cast(Level level, Player player, ItemStack stack) throws InterruptedException {
        // default: does nothing
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            try {
                cast(level, player, stack);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}