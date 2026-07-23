package bunger.group.tyler.item;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler.item.ModItems;
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
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "squirrel_tab")
    );

    public static final CreativeModeTab MOD_TAB = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            MOD_TAB_KEY,
            CreativeModeTab.builder(CreativeModeTab.Row.TOP,6)
                    .title(Component.translatable("itemGroup.mutually-assured-destruction.squirrel_tab"))
                    .icon(() -> ModItems.SQUIRREL_STAPELER.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Sticks
                        output.accept(ModItems.SQUIRREL_STAPELER);
                        output.accept(ModItems.SQUIRREL_GUN);
                        output.accept(ModItems.SQUEATHER_CHEST);
                        output.accept(ModItems.SQUEATHER_LEGS);
                        output.accept(ModItems.SQUEATHER_HEAD);
                        output.accept(ModItems.SQUEATHER_FEET);

                    })
                    .build()
    );

    public static void registerCreativeTabs() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering Creative Tabs for " + MutuallyAssuredDestruction.MOD_ID);
        var tab = MOD_TAB;
    }
}