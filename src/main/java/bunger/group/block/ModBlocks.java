package bunger.group.block;
import bunger.group.MutuallyAssuredDestruction;
import bunger.group.item.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Material;

public class ModBlocks {
    public static final Block TANZANITE_BLOCK = registerBlock("tanzanite_block",
            new Block(FabricBlockSettings.of(Material.METAL).strength(4f).requiresTool()), ModItemGroup.TANZANITE);

    private static Block registerBlock(String name, Block block, CreativeModeTab tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block, CreativeModeTab tab) {
        return Registry.register(Registry.ITEM, new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering ModBlocks for " + MutuallyAssuredDestruction.MOD_ID);
    }
}
