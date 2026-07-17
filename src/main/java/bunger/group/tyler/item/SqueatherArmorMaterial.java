package bunger.group.tyler.item;

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

public class SqueatherArmorMaterial {
    public static final int BASE_DURABILITY = 15;
    public static final ResourceKey<EquipmentAsset> SQUEATHER_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "squeather"));
    public static final TagKey<Item> REPAIRS_SQUEATHER_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_squeather_armor"));
    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 3,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.BOOTS, 3
            ),
            5,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            REPAIRS_SQUEATHER_ARMOR,
            SQUEATHER_ARMOR_MATERIAL_KEY
    );
}
