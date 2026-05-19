package bunger.group.alex;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.block.ModBlocks;
import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;



public class CreativeTab {


    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "wizardry_tab")
    );


    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.AGARTHAN_THUNDER))
            .title(Component.translatable("creativeTab.wizardry-tab"))
            .displayItems((parameters, output) -> {
                // Items
                // Agarthan Spells
                output.accept(ModItems.AGARTHAN_ICE_DOME);
                output.accept(ModItems.AGARTHAN_THUNDER);
                // Lightning
                output.accept(ModItems.ZAP);
                output.accept(ModItems.LIGHTNING);
                output.accept(ModItems.CHANNEL_STORM);
                output.accept(ModItems.BLANK_LIGHTNING_SCROLL);
                // Ice
                output.accept(ModItems.ICE_SHIELD);
                output.accept(ModItems.BLANK_ICE_SCROLL);
                // Fire
                output.accept(ModItems.IGNITION);
                output.accept(ModItems.BLANK_FIRE_SCROLL);
                // Earth
                output.accept(ModItems.IMPALE);
                output.accept(ModItems.FOREST_OF_SPIKES);
                output.accept(ModItems.BLANK_EARTH_SCROLL);
                // Water
                output.accept(ModItems.SUMMON_WATER);
                output.accept(ModItems.INVOKE_RAIN);
                output.accept(ModItems.TSUNAMI);
                output.accept(ModItems.BLANK_WATER_SCROLL);
                // Poison
                output.accept(ModItems.POISON_WAVE);
                output.accept(ModItems.POISON_RAIN);
                output.accept(ModItems.BLANK_POISON_SCROLL);
                // Staff
                output.accept(ModItems.STAFF_OF_TELEPORTATION);
                // Other items
                output.accept(ModItems.PURE_MANA);

                // Armour
                output.accept(ModItems.CLOTH_HELMET);
                output.accept(ModItems.CLOTH_CHESTPLATE);
                output.accept(ModItems.CLOTH_LEGGINGS);
                output.accept(ModItems.CLOTH_BOOTS);

                output.accept(ModItems.GOBLIN_CROWN);

                output.accept(ModItems.SKELETON_CHESTPLATE);
                output.accept(ModItems.ZOMBIE_LEGGINGS);
                output.accept(ModItems.SPIDER_BOOTS);

                // Blocks
                output.accept(ModBlocks.SPELL_DESK);
            })
            .build();


    public static void register() {
        //Add creative tab
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
    }

}
