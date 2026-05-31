package bunger.group.alex.item.armor;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
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

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;



public class SpiderBoots extends Item {

    private static final float FALL_DAMAGE_REDUCTION = 0.50F;

    private static final Identifier FALL_DAMAGE_MULT_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "spider_boots_fall_mult");

    public SpiderBoots(Properties properties) {
        super(properties
                .humanoidArmor(SpiderArmourMaterial.INSTANCE, ArmorType.BOOTS)
                .durability(ArmorType.BOOTS.getDurability(SpiderArmourMaterial.BASE_DURABILITY))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Surprisingly squishy").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("50% Fall Damage Reduction").withStyle(ChatFormatting.DARK_GREEN));
    }


    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof LivingEntity living)) return;

        if (slot == EquipmentSlot.FEET) {
            // Add modifiers if not present
            var attr = living.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER);
            if (attr != null && attr.getModifier(FALL_DAMAGE_MULT_MODIFIER_ID) == null) {
                attr.addTransientModifier(new AttributeModifier(
                        FALL_DAMAGE_MULT_MODIFIER_ID,
                        -FALL_DAMAGE_REDUCTION,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }

        } else {
            var attr = living.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER);
            if (attr != null) attr.removeModifier(FALL_DAMAGE_MULT_MODIFIER_ID);
        }
    }


    private static class SpiderArmourMaterial {

        public static final int BASE_DURABILITY = 30; // boots = base * 13 → 390

        public static final ResourceKey<EquipmentAsset> SPIDER_ARMOR_MATERIAL_KEY =
                ResourceKey.create(EquipmentAssets.ROOT_ID,
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "spider_armour"));

        public static final TagKey<Item> REPAIRS_SPIDER_ARMOR =
                TagKey.create(BuiltInRegistries.ITEM.key(),
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_spider_armour"));

        public static final ArmorMaterial INSTANCE = new ArmorMaterial(
                BASE_DURABILITY,
                Map.of(
                        ArmorType.BOOTS,      2
                ),
                22,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                0.0F,
                0.0F,
                REPAIRS_SPIDER_ARMOR,
                SPIDER_ARMOR_MATERIAL_KEY
        );
    }
}