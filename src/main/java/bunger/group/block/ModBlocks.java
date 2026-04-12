package bunger.group.block;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class ModBlocks {

    public static final Block SUMMONING_CIRCLE_NW = registerBlock("summoning_circle_nw",
            new SummoningCircle(FabricBlockSettings.of(Material.CLOTH_DECORATION)
                    .strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noCollission()
                    .noOcclusion()));

    public static final Block SUMMONING_CIRCLE_NE = registerBlock("summoning_circle_ne",
            new SummoningCircle(FabricBlockSettings.of(Material.CLOTH_DECORATION)
                    .strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noCollission()
                    .noOcclusion()));

    public static final Block SUMMONING_CIRCLE_SW = registerBlock("summoning_circle_sw",
            new SummoningCircle(FabricBlockSettings.of(Material.CLOTH_DECORATION)
                    .strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noCollission()
                    .noOcclusion()));

    public static final Block SUMMONING_CIRCLE_SE = registerBlock("summoning_circle_se",
            new SummoningCircle(FabricBlockSettings.of(Material.CLOTH_DECORATION)
                    .strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noCollission()
                    .noOcclusion()));
    public static final Block BLOOD_TEXT = registerBlock("blood_text",
            new BloodTextWall(FabricBlockSettings.of(Material.DECORATION)
                    .strength(0.1f)
                    .noCollission()
                    .noOcclusion()));
    private static Block registerBlock(String name, Block block) {
        return Registry.register(
                Registry.BLOCK,
                new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, name),
                block);
    }

    public static void registerModBlocks() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering blocks for "
                + MutuallyAssuredDestruction.MOD_ID);
    }
}