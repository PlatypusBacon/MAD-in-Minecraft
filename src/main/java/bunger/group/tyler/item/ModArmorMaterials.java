package bunger.group.tyler.item;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class ModArmorMaterials {

    // Points to assets/<modid>/equipment/squeather.json
    public static final ResourceKey<EquipmentAsset> SQUEATHER_KEY = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "squeather")
    );

    public static final ArmorMaterial SQUEATHER = new ArmorMaterial(
            100,                                    // base durability
            Map.of(
                    ArmorType.HELMET,     1,
                    ArmorType.CHESTPLATE, 1,
                    ArmorType.LEGGINGS,   1,
                    ArmorType.BOOTS,      1
            ),
            0,                                      // enchantmentValue
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0f,                                     // toughness
            0f,                                     // knockbackResistance
            TagKey.create(                          // repair ingredient tag (can use any tag, or a dummy one)
                    BuiltInRegistries.ITEM.key(),
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_squeather")
            ),
            SQUEATHER_KEY
    );

    public static void initialize() {
        MutuallyAssuredDestruction.LOGGER.info("Registering " + MutuallyAssuredDestruction.MOD_ID + " armor materials");

    }
}