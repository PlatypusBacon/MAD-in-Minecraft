package bunger.group.lizz;

import net.minecraft.world.item.component.ItemLore;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;



import java.util.ArrayList;
import java.util.function.Function;

import org.jspecify.annotations.Nullable;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.resources.Identifier;


public class FB extends Item {
    public FB(Item.Properties properties) {
    super(properties);
    }
    
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
          ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
    
          T item = itemFactory.apply(settings.setId(itemKey));
    
          Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    
          return item;
    }
    
    public static final Item FB_Item = register("friendship_bracelet", FB::new, new Item.Properties().durability(100));

   public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(FB.FB_Item));
    }

@Override
public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
    super.inventoryTick(itemStack, level, owner, slot);

    if (owner instanceof LivingEntity livingEntity) {

        // Current holder name
        String currentOwnerName = livingEntity.getName().getString();

        // Read lore from item
        ItemLore lore = itemStack.get(DataComponents.LORE);
        String creatorName = "";

        if (lore != null && !lore.lines().isEmpty()) {
            creatorName = lore.lines().get(0).getString().replace("Made by: ", "");
        }

        // Only activate if someone else is holding it
        if (!currentOwnerName.equals(creatorName)) {

            livingEntity.addEffect(
                new MobEffectInstance(MobEffects.REGENERATION, 20, 0, false, false)
            );

            // Damage once per second
            if (level.getGameTime() % 20 == 0) {
                itemStack.hurtAndBreak(1, livingEntity, slot);
            }
        }
    }
}

@Override
public void onCraftedBy(ItemStack itemStack, Player player) {
    super.onCraftedBy(itemStack, player);

    List<Component> lines = new ArrayList<>();
    lines.add(Component.literal("Made by: " + player.getName().getString()));

    itemStack.set(DataComponents.LORE, new ItemLore(lines));
}


}