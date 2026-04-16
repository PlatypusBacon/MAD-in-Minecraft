package bunger.group.tyler.block;

import bunger.group.MutuallyAssuredDestruction;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {


    public static final Block SUMMONING_CIRCLE_NW = SummoningCircle.registerBlock("summoning_circle_nw",
            SummoningCircle::new,
            BlockBehaviour.Properties.of().strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .noCollision(), true
    );
    public static final Block SUMMONING_CIRCLE_NE = SummoningCircle.registerBlock("summoning_circle_ne",
            SummoningCircle::new,
            BlockBehaviour.Properties.of().strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .noCollision(), true
    );
    public static final Block SUMMONING_CIRCLE_SE = SummoningCircle.registerBlock("summoning_circle_se",
            SummoningCircle::new,
            BlockBehaviour.Properties.of().strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .noCollision(), true
    );
    public static final Block SUMMONING_CIRCLE_SW = SummoningCircle.registerBlock("summoning_circle_sw",
            SummoningCircle::new,
            BlockBehaviour.Properties.of().strength(0.1f)
                    .sound(SoundType.WOOL)
                    .noOcclusion()
                    .noCollision(), true
    );

    public static void registerModBlocks() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering blocks for "
                + MutuallyAssuredDestruction.MOD_ID);
    }
}