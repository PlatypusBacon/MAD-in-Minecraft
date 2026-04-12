package bunger.group.item;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item SQUIRREL_GUN = registerItem("squirrel_gun",
            new SquirrelGunItem(new FabricItemSettings().group(ModItemGroup.TANZANITE)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(
                Registry.ITEM,
                new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, name),
                item
        );
    }

    public static void registerModItems() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering Mod Items for " + MutuallyAssuredDestruction.MOD_ID);
    }
}