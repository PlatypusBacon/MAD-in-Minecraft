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
                output.accept(ModItems.ICE_SHIELD);
                output.accept(ModItems.AGARTHAN_THUNDER);
                output.accept(ModItems.ZAP);
                output.accept(ModItems.AGARTHAN_ICE_DOME);
                output.accept(ModItems.IGNITION);
                output.accept(ModItems.IMPALE);
                output.accept(ModItems.STAFF_OF_TELEPORTATION);
                output.accept(ModItems.BLANK_LIGHTNING_SCROLL);
                output.accept(ModItems.BLANK_FIRE_SCROLL);
                output.accept(ModItems.BLANK_ICE_SCROLL);
                output.accept(ModItems.PURE_MANA);

                // Armour
                output.accept(ModItems.CLOTH_HELMET);
                output.accept(ModItems.CLOTH_CHESTPLATE);
                output.accept(ModItems.CLOTH_LEGGINGS);
                output.accept(ModItems.CLOTH_BOOTS);

                // Blocks
                output.accept(ModBlocks.SPELL_DESK);
            })
            .build();


    public static void register() {
        //Add creative tab
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
    }

}
