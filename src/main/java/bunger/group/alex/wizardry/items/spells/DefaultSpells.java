package bunger.group.alex.wizardry.items.spells;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DefaultSpells {

    public static final Item BASIC_SCROLL = Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", "basic_scroll"),
            new ScrollItem(new Item.Properties())
    );

    public static void register() {}
}