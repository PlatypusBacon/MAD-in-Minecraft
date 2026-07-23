package bunger.group.alex.item.armour;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.*;

import java.util.Map;
import java.util.function.Consumer;

public class SkeletonChestplate extends Item {

    private static final Identifier PROJECTILE_DAMAGE_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "skeleton_chest_projectile_damage");

    public SkeletonChestplate(Properties properties) {
        super(properties
                .humanoidArmor(SkeletonArmourMaterial.INSTANCE, ArmorType.CHESTPLATE)
                .durability(ArmorType.CHESTPLATE.getDurability(SkeletonArmourMaterial.BASE_DURABILITY))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("A bit bony").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("+25% Arrow Damage").withStyle(ChatFormatting.DARK_GREEN));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof LivingEntity living)) return;

        if (slot == EquipmentSlot.CHEST) {
            var attr = living.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER);
            if (attr != null && attr.getModifier(PROJECTILE_DAMAGE_MODIFIER_ID) == null) {
                attr.addTransientModifier(new AttributeModifier(
                        PROJECTILE_DAMAGE_MODIFIER_ID,
                        0.25,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        } else {
            var attr = living.getAttribute(Attributes.FALL_DAMAGE_MULTIPLIER);
            if (attr != null) attr.removeModifier(PROJECTILE_DAMAGE_MODIFIER_ID);
        }
    }


    private static class SkeletonArmourMaterial {

        public static final int BASE_DURABILITY = 30;

        public static final ResourceKey<EquipmentAsset> SKELETON_ARMOR_MATERIAL_KEY =
                ResourceKey.create(EquipmentAssets.ROOT_ID,
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "skeleton_armour"));

        public static final TagKey<Item> REPAIRS_SKELETON_ARMOR =
                TagKey.create(BuiltInRegistries.ITEM.key(),
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_skeleton_armor"));

        public static final ArmorMaterial INSTANCE = new ArmorMaterial(
                BASE_DURABILITY,
                Map.of(
                        ArmorType.CHESTPLATE, 5
                ),
                22,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                0.0F,
                0.0F,
                REPAIRS_SKELETON_ARMOR,
                SKELETON_ARMOR_MATERIAL_KEY
        );
    }
}