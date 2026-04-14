package bunger.group.alex.wizardry.items.spells;

import bunger.group.alex.wizardry.ModItemGroups;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScrollItem extends Item {

    public ScrollItem(Properties properties) {
        super(properties.tab(ModItemGroups.WIZARDRY_TAB));
    }

    public void cast(Level level, LivingEntity caster, ItemStack stack) {
        // default: does nothing
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            cast(level, player, stack);
        }
        return InteractionResultHolder.success(stack);
    }
}