package bunger.group.tyler3.block;

import bunger.group.MutuallyAssuredDestruction;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final Block PLATFORM_TRACK_BLOCK = PlatformTrackBlock.registerBlock("platform_track",
            PlatformTrackBlock::new,
            BlockBehaviour.Properties.of().strength(5.0f)
                    .sound(SoundType.ANVIL), true
    );
    public static void registerModBlocks() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering goop " + MutuallyAssuredDestruction.MOD_ID
        );
        // No world-placed blocks for tyler3 — the attachment is data-only.
        // Add future track blocks here.
    }
}