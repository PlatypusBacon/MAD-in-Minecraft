package bunger.group.tyler3.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.item.StickyGooItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item STICKY_GOO = register(
            "sticky_goo",
            StickyGooItem::new,
            new Item.Properties()
    );
    public static final Item PLATFORM = register(
            "platform",
            PlatformItem::new,
            new Item.Properties()
    );

    private static <T extends Item> T register(
            String name,
            java.util.function.Function<Item.Properties, T> factory,
            Item.Properties props
    ) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name)
        );
        T item = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering tyler3 items for " + MutuallyAssuredDestruction.MOD_ID
        );
        var sticky = STICKY_GOO;
        var plat = PLATFORM;
    }
}