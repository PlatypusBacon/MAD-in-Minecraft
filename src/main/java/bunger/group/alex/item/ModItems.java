package bunger.group.alex.item;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ModItems {

    public static final Item ICE_SHIELD = registerItem(
            "spell_ice_ice_shield",
            IceShield::new, // funky lambda I am not that sure of
            new IceShield.Properties()
    );

    public static final Item AGARTHAN_THUNDER = registerItem(
            "spell_lightning_agarthan_thunder",
            AgarthanThunder::new,
            new AgarthanThunder.Properties()
    );

    public static <T extends Item> T registerItem(
            String name,
            Function<Item.Properties, T> itemFactory,
            Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }


    public static void register() {}
}
