package bunger.group.ethan;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import bunger.group.MutuallyAssuredDestruction;

import java.util.List;
import java.util.Map;

public class VoremothArmorMaterial {

    public static final int BASE_DURABILITY = 40;

    public static final ResourceKey<EquipmentAsset> VOREMOTH_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "voremoth_crown"));

    public static final TagKey<Item> REPAIRS_VOREMOTH_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_guidite_armor"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
		BASE_DURABILITY,
        Map.of(
                ArmorType.HELMET, 3
        ),
        5,
        (SoundEvents.ARMOR_EQUIP_DIAMOND),
        0.0F,
        0.0F,
        REPAIRS_VOREMOTH_ARMOR,
        VOREMOTH_ARMOR_MATERIAL_KEY
    );


    // private static Holder<ArmorMaterial> register(String name,
    //         Map<ArmorType, Integer> defense,
    //         int enchantability,
    //         net.minecraft.core.Holder<net.minecraft.sounds.SoundEvent> equipSound,
    //         float toughness,
    //         float knockbackResistance,
    //         Ingredient repairIngredient) {
    //     ResourceKey<ArmorMaterial> key = ResourceKey.create(Registries.ARMOR_MATERIAL, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
    //     ArmorMaterial material = new ArmorMaterial(
    //         defense,
    //         enchantability, 
    //         equipSound,
    //         () -> repairIngredient, 
    //         List.of(), 
    //         toughness, 
    //         knockbackResistance
    //     );
    //     return Registry.register(BuiltInRegistries.ARMOR_MATERIAL, key, material);
    // }

    public static void register() {}
}