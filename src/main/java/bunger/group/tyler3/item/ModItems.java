package bunger.group.tyler3.item;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {
    public static final Item SHOPPING_CART = register("shopping_cart", ShoppingCartItem::new, new ShoppingCartItem.Properties());
    public static final Item GOLD_SCAR = register("gold_scar", GoldScarItem::new,
            new GoldScarItem.Properties().component(DataComponents.ITEM_MODEL,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "gold_scar")));
    public static final Item MEDIUM_AMMO = register("medium_ammo", MediumAmmoItem::new, new MediumAmmoItem.Properties());

    private static <T extends Item> T register(String name,
                                               Function<Item.Properties, T> factory,
                                               Item.Properties props) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        T item = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
    public static void registerModItems() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering Mod Items for " + MutuallyAssuredDestruction.MOD_ID);
        var sc = SHOPPING_CART;
    }
}
