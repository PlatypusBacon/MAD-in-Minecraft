package bunger.group.item;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.EquipmentSlot;

public class SqueatherMaterial implements ArmorMaterial {

    // This name drives the texture path:
    // textures/models/armor/squeather_layer_1.png
    // textures/models/armor/squeather_layer_2.png
    private static final String NAME = "squeather";

    @Override public String getName() {

        return "mutually-assured-destruction:squeather"; }

    @Override public int getDurabilityForSlot(EquipmentSlot slot) { return 100; }
    @Override public int getDefenseForSlot(EquipmentSlot slot) { return 1; }
    @Override public int getEnchantmentValue() { return 0; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_LEATHER; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }
    @Override public float getToughness() { return 0f; }
    @Override public float getKnockbackResistance() { return 0f; }
}