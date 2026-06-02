package bunger.group.alex.item.armour;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.*;

import java.util.Map;
import java.util.function.Consumer;

public class EnchantedChainArmour extends Item {

    ArmorType slot;
    int maxManaInc;

    public EnchantedChainArmour(Properties properties, ArmorType slot) {
        Properties finalProps = properties
                .humanoidArmor(EnchantedChainArmourMaterial.INSTANCE, slot)
                .durability(slot.getDurability(EnchantedChainArmourMaterial.BASE_DURABILITY));

        if (slot == ArmorType.HELMET) {
            finalProps = finalProps.component(
                    DataComponents.EQUIPPABLE,
                    Equippable.builder(slot.getSlot()).build()
            );
        }

        super(finalProps);

        this.slot = slot;

        switch (slot) {
            case HELMET, BOOTS -> this.maxManaInc = 10;
            case CHESTPLATE, LEGGINGS -> this.maxManaInc = 15;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Brightly polished chain").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("+" + this.maxManaInc + " Max Mana").withStyle(ChatFormatting.DARK_GREEN));
    }


    public int getMaxManaBonus() {
        return maxManaInc;
    }

    public EquipmentSlot getSlot() {
        return this.slot.getSlot();
    }

    private class EnchantedChainArmourMaterial {
        public static final int BASE_DURABILITY = 14;
        public static final ResourceKey<EquipmentAsset> ENCHANTED_CHAIN_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "enchanted_chain"));
        public static final TagKey<Item> REPAIRS_ENCHANTED_CHAIN_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_enchanted_chain_armor"));
        public static final ArmorMaterial INSTANCE = new ArmorMaterial(
                BASE_DURABILITY,
                Map.of(
                        ArmorType.HELMET, 1,
                        ArmorType.CHESTPLATE, 4,
                        ArmorType.LEGGINGS, 2,
                        ArmorType.BOOTS, 1
                ),
                18,
                SoundEvents.ARMOR_EQUIP_CHAIN,
                0.0F,
                0.0F,
                REPAIRS_ENCHANTED_CHAIN_ARMOR,
                ENCHANTED_CHAIN_ARMOR_MATERIAL_KEY
        );
    }
}

