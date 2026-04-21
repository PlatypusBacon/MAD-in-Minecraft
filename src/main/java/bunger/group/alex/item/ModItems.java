package bunger.group.alex.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.item.armor.ClothArmorMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;

public class ModItems {

    //Spells
    public static final Item ICE_SHIELD = registerItem(
            "spell_ice_ice_shield",
            IceShield::new, // funky lambda I am not that sure of
            new IceShield.Properties()
    );

    public static final Item AGARTHAN_ICE_DOME = registerItem(
            "spell_ice_agarthan_ice_dome",
            AgarthanIceDome::new, // funky lambda I am not that sure of
            new AgarthanIceDome.Properties()
    );

    public static final Item ZAP = registerItem(
            "zap",
            Zap::new,
            new Zap.Properties()
    );
    public static final Item AGARTHAN_THUNDER = registerItem(
            "spell_lightning_agarthan_thunder",
            AgarthanThunder::new,
            new AgarthanThunder.Properties()
    );

    public static final Item IGNITION = registerItem(
            "spell_fire_ignition",
            Ignition::new,
            new Ignition.Properties()
    );

    public static final Item STAFF_OF_TELEPORTATION = registerItem(
            "spell_staff_of_teleportation",
            StaffOfTeleportation::new,
            new StaffOfTeleportation.Properties()
    );

    // Blank Spells

    public static final Item BLANK_LIGHTNING_SCROLL = registerItem(
            "blank_lightning_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.LIGHTNING),
            new BlankScroll.Properties()
    );

    public static final Item BLANK_FIRE_SCROLL = registerItem(
            "blank_fire_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.FIRE),
            new BlankScroll.Properties()
    );

    public static final Item BLANK_ICE_SCROLL = registerItem(
            "blank_ice_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.ICE),
            new BlankScroll.Properties()
    );

    // Materials
    public static final Item PURE_MANA = registerItem(
            "eitr",
            Item::new,
            new Item.Properties()
    );



    // Armour
    public static final Item CLOTH_HELMET = registerItem(
            "cloth_helmet",
            Item::new,
            new Item.Properties().humanoidArmor(ClothArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(ClothArmorMaterial.BASE_DURABILITY))
    );

    public static final Item CLOTH_CHESTPLATE = registerItem(
            "cloth_chestplate",
            Item::new,
            new Item.Properties().humanoidArmor(ClothArmorMaterial.INSTANCE, ArmorType.CHESTPLATE)
                    .durability(ArmorType.CHESTPLATE.getDurability(ClothArmorMaterial.BASE_DURABILITY))
    );

    public static final Item CLOTH_LEGGINGS = registerItem(
            "cloth_leggings",
            Item::new,
            new Item.Properties().humanoidArmor(ClothArmorMaterial.INSTANCE, ArmorType.LEGGINGS)
                    .durability(ArmorType.LEGGINGS.getDurability(ClothArmorMaterial.BASE_DURABILITY))
    );

    public static final Item CLOTH_BOOTS = registerItem(
            "cloth_boots",
            Item::new,
            new Item.Properties().humanoidArmor(ClothArmorMaterial.INSTANCE, ArmorType.BOOTS)
                    .durability(ArmorType.BOOTS.getDurability(ClothArmorMaterial.BASE_DURABILITY))
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
