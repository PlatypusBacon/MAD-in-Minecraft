package bunger.group.ethan;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.resources.Identifier;


public class Bunger4 {
   //public static final Item CUSTOM_ITEM = new Item(new Item.Properties());

   public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));

		// Create the item instance.
		T item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
   }

   public static final Item SUSPICIOUS_SUBSTANCE = register("suspicious_substance", Item::new, new Item.Properties());



   public static void initialize() {
        // Get the event for modifying entries in the ingredients group.
        // And register an event handler that adds our suspicious item to the ingredients group.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(Bunger4.SUSPICIOUS_SUBSTANCE));
    }


}


