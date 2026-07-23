package bunger.group.csmit863;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.block.ModBlocks;
import bunger.group.csmit863.item.ModItems;
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
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mushroom_mania")
    );


    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.MAGIC_MUSHROOM))
            .title(Component.translatable("creativeTab.mushroom_mania_tab"))
            .displayItems((parameters, output) -> {
                // Items
                output.accept(ModItems.MAGIC_MUSHROOM);

                // Blocks
                output.accept(ModBlocks.HALLUCINITE_BLOCK);
                output.accept(ModBlocks.MAGIC_MUSHROOM_BLOCK);
            })
            .build();


    public static void register() {
        //Add creative tab
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
    }

}
