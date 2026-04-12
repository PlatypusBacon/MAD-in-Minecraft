package bunger.group.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import bunger.group.item.ModItems;
import bunger.group.block.ModBlocks;

public class ModItemGroup {

    public static final CreativeModeTab TANZANITE = new CreativeModeTab(0, "tanzanite_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.TANZANITE_BLOCK);
        }
    };
}