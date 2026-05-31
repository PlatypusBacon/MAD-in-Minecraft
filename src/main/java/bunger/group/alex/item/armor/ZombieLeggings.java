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

public class ZombieLeggings extends Item {

    public ZombieLeggings(Properties properties) {
        super(properties
                .humanoidArmor(ZombieArmourMaterial.INSTANCE, ArmorType.LEGGINGS)
                .durability(ArmorType.LEGGINGS.getDurability(ZombieArmourMaterial.BASE_DURABILITY))
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Rotten, but quite sturdy").withStyle(ChatFormatting.GRAY));
    }

    private static class ZombieArmourMaterial {

        public static final int BASE_DURABILITY = 30;

        public static final ResourceKey<EquipmentAsset> ZOMBIE_LEGGINGS_MATERIAL_KEY =
                ResourceKey.create(EquipmentAssets.ROOT_ID,
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "zombie_armour"));

        public static final TagKey<Item> REPAIRS_ZOMBIE_LEGGINGS =
                TagKey.create(BuiltInRegistries.ITEM.key(),
                        Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "repairs_zombie_leggings"));

        public static final ArmorMaterial INSTANCE = new ArmorMaterial(
                BASE_DURABILITY,
                Map.of(
                        ArmorType.LEGGINGS,   6
                ),
                22,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                1.0F,
                0.0F,
                REPAIRS_ZOMBIE_LEGGINGS,
                ZOMBIE_LEGGINGS_MATERIAL_KEY
        );
    }
}