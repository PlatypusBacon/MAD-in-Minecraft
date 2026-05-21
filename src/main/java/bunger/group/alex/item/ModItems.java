package bunger.group.alex.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.item.armor.*;
import bunger.group.alex.item.spell.*;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Function;

public class ModItems {

    //Spells
    public static final Item ICE_SHIELD = registerItem(
            "spell_ice_ice_shield",
            IceShield::new, // funky lambda I am not that sure of
            new IceShield.Properties()
    );

    public static final Item FREEZE = registerItem(
            "spell_ice_freeze",
            Freeze::new,
            new Freeze.Properties()
    );


    public static final Item AGARTHAN_ICE_DOME = registerItem(
            "spell_ice_agarthan_ice_dome",
            AgarthanIceDome::new,
            new AgarthanIceDome.Properties()
    );

    public static final Item ZAP = registerItem(
            "spell_lightning_zap",
            Zap::new,
            new Zap.Properties()
    );

    public static final Item LIGHTNING = registerItem(
            "spell_lightning_lightning",
            Lightning::new,
            new Lightning.Properties()
    );

    public static final Item CHANNEL_STORM = registerItem(
            "spell_lightning_channel_storm",
            ChannelStorm::new,
            new ChannelStorm.Properties()
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

    public static final Item IMPALE = registerItem(
            "spell_earth_impale",
            Impale::new,
            new Impale.Properties()
    );

    public static final Item FOREST_OF_SPIKES = registerItem(
            "spell_earth_forest_of_spikes",
            ForestOfSpikes::new,
            new ForestOfSpikes.Properties()
    );

    public static final Item SUMMON_WATER = registerItem(
            "spell_water_summon_water",
            SummonWater::new,
            new SummonWater.Properties()
    );

    public static final Item INVOKE_RAIN = registerItem(
            "spell_water_invoke_rain",
            InvokeRain::new,
            new InvokeRain.Properties()
    );

    public static final Item TSUNAMI = registerItem(
            "spell_water_tsunami",
            Tsunami::new,
            new Tsunami.Properties()
    );

    public static final Item POISON_WAVE = registerItem(
            "spell_poison_wave",
            PoisonWave::new,
            new PoisonWave.Properties()
    );

    public static final Item POISON_RAIN = registerItem(
            "spell_poison_rain",
            PoisonRain::new,
            new PoisonRain.Properties()
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

    public static final Item BLANK_EARTH_SCROLL = registerItem(
            "blank_earth_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.EARTH),
            new BlankScroll.Properties()
    );

    public static final Item BLANK_WATER_SCROLL = registerItem(
            "blank_water_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.WATER),
            new BlankScroll.Properties()
    );

    public static final Item BLANK_POISON_SCROLL = registerItem(
            "blank_poison_scroll",
            (Item.Properties properties) -> new BlankScroll(properties, SpellTypes.POISON),
            new BlankScroll.Properties()
    );

    // Materials
    public static final Item PURE_MANA = registerItem(
            "eitr",
            Item::new,
            new Item.Properties()
    );

    public static final Item CLOTH = registerItem(
            "cloth_item",
            Item::new,
            new Item.Properties()
    );

    // Armour
    public static final Item CLOTH_HELMET = registerItem(
            "cloth_helmet",
            Item::new,
            new Item.Properties()
                    .humanoidArmor(ClothArmorMaterial.INSTANCE, ArmorType.HELMET)
                    .durability(ArmorType.HELMET.getDurability(ClothArmorMaterial.BASE_DURABILITY))
                    .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                            .setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
                            .build())
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

    public static final Item GOBLIN_CROWN = registerItem(
            "goblin_crown",
            GoblinCrown::new,
            new GoblinCrown.Properties()
    );

    public static final Item SPIDER_BOOTS = registerItem(
            "spider_boots",
            SpiderBoots::new,
            new SpiderBoots.Properties()
    );

    public static final Item ZOMBIE_LEGGINGS = registerItem(
            "zombie_leggings",
            ZombieLeggings::new,
            new ZombieLeggings.Properties()
    );

    public static final Item SKELETON_CHESTPLATE = registerItem(
            "skeleton_chestplate",
            SkeletonChestplate::new,
            new SkeletonChestplate.Properties()
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
