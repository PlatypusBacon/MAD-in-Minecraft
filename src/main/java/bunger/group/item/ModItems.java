package bunger.group.item;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final Item SQUIRREL_GUN = registerItem("squirrel_gun",
            new SquirrelGunItem(new FabricItemSettings()));

    public static final Item SQUEATHER_HEAD  = registerItem("squeather_head",
            new SqueatherItem(new FabricItemSettings(), EquipmentSlot.HEAD));

    public static final Item SQUEATHER_CHEST = registerItem("squeather_chest",
            new SqueatherItem(new FabricItemSettings(), EquipmentSlot.CHEST));

    public static final Item SQUEATHER_LEGS  = registerItem("squeather_legs",
            new SqueatherItem(new FabricItemSettings(), EquipmentSlot.LEGS));

    public static final Item SQUEATHER_FEET  = registerItem("squeather_feet",
            new SqueatherItem(new FabricItemSettings(), EquipmentSlot.FEET));

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