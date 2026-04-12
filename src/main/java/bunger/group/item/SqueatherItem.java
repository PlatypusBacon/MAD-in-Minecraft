package bunger.group.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;

public class SqueatherItem extends Item implements Wearable {

    private final EquipmentSlot slot;

    public SqueatherItem(Properties properties, EquipmentSlot slot) {
        super(properties);
        this.slot = slot;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ItemStack current = player.getItemBySlot(this.slot);

        if (current.isEmpty()) {
            player.setItemSlot(this.slot, stack.copy());
            stack.setCount(0);
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.fail(stack);
    }
}