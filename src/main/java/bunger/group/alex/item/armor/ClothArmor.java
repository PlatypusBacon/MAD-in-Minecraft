package bunger.group.alex.item.armor;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.Mana;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.*;

import java.util.Map;
import java.util.function.Consumer;

public class ClothArmor extends Item {

    ArmorType slot;
    int maxManaInc;

    public ClothArmor(Properties properties, ArmorType slot) {
        Properties finalProps = properties
                .humanoidArmor(ClothArmorMaterial.INSTANCE, slot)
                .durability(slot.getDurability(ClothArmorMaterial.BASE_DURABILITY));

        if (slot == ArmorType.HELMET) {
            finalProps = finalProps.component(
                    DataComponents.EQUIPPABLE,
                    Equippable.builder(slot.getSlot()).build()
            );
        }

        super(finalProps);

        this.slot = slot;

        switch (slot) {
            case HELMET, BOOTS -> this.maxManaInc = 5;
            case CHESTPLATE, LEGGINGS -> this.maxManaInc = 10;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Fitting for the finest peasant").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("+" + this.maxManaInc + " Max Mana").withStyle(ChatFormatting.DARK_GREEN));
    }


    public int getMaxManaBonus() {
        return maxManaInc;
    }

    public EquipmentSlot getSlot() {
        return this.slot.getSlot();
    }

    private class ClothArmorMaterial {
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
}

