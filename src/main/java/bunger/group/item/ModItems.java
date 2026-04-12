package bunger.group.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
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
    public static final Item SUMMONING_CIRCLE_NW = registerItem("summoning_circle_nw",
            new BlockItem(ModBlocks.SUMMONING_CIRCLE_NW, new FabricItemSettings()));
    public static final Item SUMMONING_CIRCLE_NE = registerItem("summoning_circle_ne",
            new BlockItem(ModBlocks.SUMMONING_CIRCLE_NE, new FabricItemSettings()));
    public static final Item SUMMONING_CIRCLE_SW = registerItem("summoning_circle_sw",
            new BlockItem(ModBlocks.SUMMONING_CIRCLE_SW, new FabricItemSettings()));
    public static final Item SUMMONING_CIRCLE_SE = registerItem("summoning_circle_se",
            new BlockItem(ModBlocks.SUMMONING_CIRCLE_SE, new FabricItemSettings()));
    public static final Item BLOOD_TEXT = registerItem("blood_text",
            new BlockItem(ModBlocks.BLOOD_TEXT, new FabricItemSettings()));
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