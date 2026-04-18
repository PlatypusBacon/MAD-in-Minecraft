package bunger.group.tyler.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;


public class ModItems {

    public static final Item SQUEATHER_HEAD = register(
            "squeather_head",
            Item::new,
            new Item.Properties().humanoidArmor(SqueatherArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(SqueatherArmorMaterial.BASE_DURABILITY))
    );
    public static final Item SQUEATHER_CHEST = register("squeather_chest",
            Item::new,
            new Item.Properties().humanoidArmor(SqueatherArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(SqueatherArmorMaterial.BASE_DURABILITY))
    );

    public static final Item SQUEATHER_LEGS = register(
            "squeather_legs",
            Item::new,
            new Item.Properties().humanoidArmor(SqueatherArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(SqueatherArmorMaterial.BASE_DURABILITY))
    );

    public static final Item SQUEATHER_FEET = register(
            "squeather_feet",
            Item::new,
            new Item.Properties().humanoidArmor(SqueatherArmorMaterial.INSTANCE, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(SqueatherArmorMaterial.BASE_DURABILITY))
    );
    public static final Item SQUIRREL_GUN = register(
            "squirrel_gun",
            SquirrelGunItem::new,
            new Item.Properties()
    );

    private static <T extends Item> T register(String name,
                                               java.util.function.Function<Item.Properties, T> factory,
                                               Item.Properties props) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name)
        );
        T item = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering Mod Items for " + MutuallyAssuredDestruction.MOD_ID
        );
        ModArmorMaterials.initialize();
        var squirrel_gun = SQUIRREL_GUN;
    }
}