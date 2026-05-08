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

public class GoblinCrown extends Item {
    public GoblinCrown(Properties properties) {
        super(properties
                .humanoidArmor(GoblinArmourMaterial.INSTANCE, ArmorType.HELMET)
                .durability(ArmorType.HELMET.getDurability(GoblinArmourMaterial.BASE_DURABILITY))
                .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                        .setEquipSound(SoundEvents.ARMOR_EQUIP_GOLD)
                        .build()));

    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Still emanates the").withStyle(ChatFormatting.GRAY));
        builder.accept(Component.literal("Goblin Chief's rage").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof LivingEntity living)) return;

        if (slot == EquipmentSlot.HEAD) {
            if (!living.hasEffect(MobEffects.STRENGTH)) {
                living.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 1, 0, false, false));
                living.addEffect(new MobEffectInstance(MobEffects.SPEED, 1, 0, false, false));
            }
        }
    }

    private static class GoblinArmourMaterial {
        public static final int BASE_DURABILITY = 50;
        public static final ResourceKey<EquipmentAsset> GOBLIN_ARMOR_MATERIAL_KEY = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "goblin_armour"));
        public static final TagKey<Item> REPAIRS_GOBLIN_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_goblin_armor"));
        public static final ArmorMaterial INSTANCE = new ArmorMaterial(
                BASE_DURABILITY,
                Map.of(
                        ArmorType.HELMET, 2,
                        ArmorType.CHESTPLATE, 6,
                        ArmorType.LEGGINGS, 6,
                        ArmorType.BOOTS, 6
                ),
                50,
                SoundEvents.ARMOR_EQUIP_GOLD,
                0.0F,
                0.0F,
                REPAIRS_GOBLIN_ARMOR,
                GOBLIN_ARMOR_MATERIAL_KEY
        );
    }
}


