package bunger.group.bryan;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.resources.Identifier;

import net.minecraft.world.item.WrittenBookItem;


public class TaxItem extends WrittenBookItem {

    public TaxItem(Item.Properties properties) {
        super(properties);
    }
    
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
          ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
    
          T item = itemFactory.apply(settings.setId(itemKey));
    
          Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    
          return item;
    }
    
    public static final Item TAX_ITEM = register("tax_item", TaxItem::new, new Item.Properties());

   public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(TaxItem.TAX_ITEM));
    }


}