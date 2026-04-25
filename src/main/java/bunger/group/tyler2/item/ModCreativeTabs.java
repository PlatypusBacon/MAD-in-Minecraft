package bunger.group.tyler2.item;


import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

public class ModCreativeTabs {

    public static final ResourceKey<CreativeModeTab> MOD_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "sticks_tab")
    );

    public static final CreativeModeTab MOD_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            MOD_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP,6)
                    .title(Component.translatable("itemGroup.mutually-assured-destruction.sticks_tab"))
                    .icon(() -> ModItems.LONG_STICK.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Sticks
                        output.accept(ModItems.THICK_STICK);
                        output.accept(ModItems.LONG_STICK);

                        // Thick pickaxes
                        output.accept(ModItems.THICK_WOODEN_PICKAXE);
                        output.accept(ModItems.THICK_STONE_PICKAXE);
                        output.accept(ModItems.THICK_IRON_PICKAXE);
                        output.accept(ModItems.THICK_GOLD_PICKAXE);
                        output.accept(ModItems.THICK_DIAMOND_PICKAXE);
                        output.accept(ModItems.THICK_NETHERITE_PICKAXE);
                        output.accept(ModItems.THICK_COPPER_PICKAXE);

                        // Thick swords
                        output.accept(ModItems.THICK_WOODEN_SWORD);
                        output.accept(ModItems.THICK_STONE_SWORD);
                        output.accept(ModItems.THICK_IRON_SWORD);
                        output.accept(ModItems.THICK_GOLD_SWORD);
                        output.accept(ModItems.THICK_DIAMOND_SWORD);
                        output.accept(ModItems.THICK_NETHERITE_SWORD);
                        output.accept(ModItems.THICK_COPPER_SWORD);

                        // Thick axes
                        output.accept(ModItems.THICK_WOODEN_AXE);
                        output.accept(ModItems.THICK_STONE_AXE);
                        output.accept(ModItems.THICK_IRON_AXE);
                        output.accept(ModItems.THICK_GOLD_AXE);
                        output.accept(ModItems.THICK_DIAMOND_AXE);
                        output.accept(ModItems.THICK_NETHERITE_AXE);
                        output.accept(ModItems.THICK_COPPER_AXE);

                        // Thick Spears
                        output.accept(ModItems.THICK_WOODEN_SPEAR);
                        output.accept(ModItems.THICK_STONE_SPEAR);
                        output.accept(ModItems.THICK_IRON_SPEAR);
                        output.accept(ModItems.THICK_GOLD_SPEAR);
                        output.accept(ModItems.THICK_DIAMOND_SPEAR);
                        output.accept(ModItems.THICK_NETHERITE_SPEAR);
                        output.accept(ModItems.THICK_COPPER_SPEAR);

                        // Thick Hoes
                        output.accept(ModItems.THICK_WOODEN_HOE);
                        output.accept(ModItems.THICK_STONE_HOE);
                        output.accept(ModItems.THICK_IRON_HOE);
                        output.accept(ModItems.THICK_GOLD_HOE);
                        output.accept(ModItems.THICK_DIAMOND_HOE);
                        output.accept(ModItems.THICK_NETHERITE_HOE);
                        output.accept(ModItems.THICK_COPPER_HOE);

                        // Thick Hoes
                        output.accept(ModItems.THICK_WOODEN_SHOVEL);
                        output.accept(ModItems.THICK_STONE_SHOVEL);
                        output.accept(ModItems.THICK_IRON_SHOVEL);
                        output.accept(ModItems.THICK_GOLD_SHOVEL);
                        output.accept(ModItems.THICK_DIAMOND_SHOVEL);
                        output.accept(ModItems.THICK_NETHERITE_SHOVEL);
                        output.accept(ModItems.THICK_COPPER_SHOVEL);

                        // Long pickaxes
                        output.accept(ModItems.LONG_WOODEN_PICKAXE);
                        output.accept(ModItems.LONG_STONE_PICKAXE);
                        output.accept(ModItems.LONG_IRON_PICKAXE);
                        output.accept(ModItems.LONG_GOLD_PICKAXE);
                        output.accept(ModItems.LONG_DIAMOND_PICKAXE);
                        output.accept(ModItems.LONG_NETHERITE_PICKAXE);
                        output.accept(ModItems.LONG_COPPER_PICKAXE);

                        // Long Axes
                        output.accept(ModItems.LONG_WOODEN_AXE);
                        output.accept(ModItems.LONG_STONE_AXE);
                        output.accept(ModItems.LONG_IRON_AXE);
                        output.accept(ModItems.LONG_GOLD_AXE);
                        output.accept(ModItems.LONG_DIAMOND_AXE);
                        output.accept(ModItems.LONG_NETHERITE_AXE);
                        output.accept(ModItems.LONG_COPPER_AXE);

                        // Long swords
                        output.accept(ModItems.LONG_WOODEN_SWORD);
                        output.accept(ModItems.LONG_STONE_SWORD);
                        output.accept(ModItems.LONG_IRON_SWORD);
                        output.accept(ModItems.LONG_GOLD_SWORD);
                        output.accept(ModItems.LONG_DIAMOND_SWORD);
                        output.accept(ModItems.LONG_NETHERITE_SWORD);
                        output.accept(ModItems.LONG_COPPER_SWORD);


                        // Long hoes
                        output.accept(ModItems.LONG_WOODEN_HOE);
                        output.accept(ModItems.LONG_STONE_HOE);
                        output.accept(ModItems.LONG_IRON_HOE);
                        output.accept(ModItems.LONG_GOLD_HOE);
                        output.accept(ModItems.LONG_DIAMOND_HOE);
                        output.accept(ModItems.LONG_NETHERITE_HOE);
                        output.accept(ModItems.LONG_COPPER_HOE);

                        // Long shovels
                        output.accept(ModItems.LONG_WOODEN_SHOVEL);
                        output.accept(ModItems.LONG_STONE_SHOVEL);
                        output.accept(ModItems.LONG_IRON_SHOVEL);
                        output.accept(ModItems.LONG_GOLD_SHOVEL);
                        output.accept(ModItems.LONG_DIAMOND_SHOVEL);
                        output.accept(ModItems.LONG_NETHERITE_SHOVEL);
                        output.accept(ModItems.LONG_COPPER_SHOVEL);

                        // long Spears
                        output.accept(ModItems.LONG_WOODEN_SPEAR);
                        output.accept(ModItems.LONG_STONE_SPEAR);
                        output.accept(ModItems.LONG_IRON_SPEAR);
                        output.accept(ModItems.LONG_GOLD_SPEAR);
                        output.accept(ModItems.LONG_DIAMOND_SPEAR);
                        output.accept(ModItems.LONG_NETHERITE_SPEAR);
                        output.accept(ModItems.LONG_COPPER_SPEAR);
                    })
                    .build()
    );

    public static void registerCreativeTabs() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering Creative Tabs for " + MutuallyAssuredDestruction.MOD_ID);
        var tab = MOD_TAB;
    }
}