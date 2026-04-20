package bunger.group.alex.item.armor;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class ClothArmorMaterial {
    public static final int BASE_DURABILITY = 10;
    public static final ResourceKey<EquipmentAsset> CLOTH_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "cloth"));
    public static final TagKey<Item> REPAIRS_CLOTH_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_cloth_armor"));
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 1,
                    ArmorType.CHESTPLATE, 1,
                    ArmorType.LEGGINGS, 1,
                    ArmorType.BOOTS, 1
            ),
            10,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            REPAIRS_CLOTH_ARMOR,
            CLOTH_ARMOR_MATERIAL_KEY
    );
}