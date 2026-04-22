package bunger.group.tyler2.block;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class ModBlocks {

    // ── Thick Torch ──────────────────────────────────────────────────────────
    // Horizontal bias: floor = 14 always, wall = 14 (horizontal is strong axis).

    /** Floor-standing thick torch. Always emits 14 — horizontal is its strong axis. */
    public static final ThickTorchBlock THICK_TORCH = registerBlock(
            "thick_torch",
            props -> new ThickTorchBlock(ParticleTypes.FLAME, props),
            torchPropsThickFloor(),
            false
    );

    /** Wall-mounted thick torch. Emits 14 on horizontal faces, 10 on vertical (defensive). */
    public static final ThickWallTorchBlock THICK_WALL_TORCH = registerBlock(
            "thick_wall_torch",
            props -> new ThickWallTorchBlock(ParticleTypes.FLAME, props),
            torchPropsThickWall(),
            false
    );

    // ── Long Torch ───────────────────────────────────────────────────────────
    // Vertical bias: floor = 14 always, wall = 10 (horizontal is weak axis).

    /** Floor-standing long torch. Always emits 14 — vertical is its strong axis. */
    public static final LongTorchBlock LONG_TORCH = registerBlock(
            "long_torch",
            props -> new LongTorchBlock(ParticleTypes.FLAME, props),
            torchPropsLongFloor(),
            false
    );

    /** Wall-mounted long torch. Emits 10 — horizontal facing fights its vertical bias. */
    public static final LongWallTorchBlock LONG_WALL_TORCH = registerBlock(
            "long_wall_torch",
            props -> new LongWallTorchBlock(ParticleTypes.FLAME, props),
            torchPropsLongWall(),
            false
    );

    // ── Light level property builders ─────────────────────────────────────────
    //
    // lightLevel(ToIntFunction<BlockState>) is the correct hook in this version.
    // The lambda is evaluated once per blockstate variant and cached in
    // BlockStateBase.lightEmission — there is no overridable method on the block.
    //
    // Floor torch variants have no FACING property, so their lambda ignores state.
    // Wall torch variants carry FACING (always horizontal), so we can branch on it.

    private static BlockBehaviour.Properties torchPropsThickFloor() {
        return baseTorchProps()
                // Thick floor torch: horizontal is the strong axis.
                // No FACING state — always emits full 14.
                .lightLevel(state -> 14);
    }

    private static BlockBehaviour.Properties torchPropsThickWall() {
        return baseTorchProps()
                // Thick wall torch: FACING is always horizontal (strong axis) → 14.
                // UP/DOWN guard is defensive; wall torches are never vertical.
                .lightLevel(state -> {
                    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                    return (facing == Direction.UP || facing == Direction.DOWN) ? 10 : 14;
                });
    }

    private static BlockBehaviour.Properties torchPropsLongFloor() {
        return baseTorchProps()
                // Long floor torch: vertical is the strong axis.
                // No FACING state — always emits full 14 (tall model channels light up).
                .lightLevel(state -> 14);
    }

    private static BlockBehaviour.Properties torchPropsLongWall() {
        return baseTorchProps()
                // Long wall torch: FACING is always horizontal (weak axis for long torch).
                // Emits 10 — wall-mounted against its preferred vertical orientation.
                // UP/DOWN guard returns 14 defensively; never reached for wall torches.
                .lightLevel(state -> {
                    Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
                    return (facing == Direction.UP || facing == Direction.DOWN) ? 14 : 10;
                });
    }

    /** Shared base properties for all torch variants. */
    private static BlockBehaviour.Properties baseTorchProps() {
        return BlockBehaviour.Properties.of().noCollision()
                .instabreak()
                .sound(SoundType.WOOD)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY);
        // NOTE: do NOT call .lightLevel() here — each variant sets its own lambda above.
    }

    // ── Registration helper ───────────────────────────────────────────────────

    public static <T extends Block> T registerBlock(
            String name,
            Function<BlockBehaviour.Properties, T> factory,
            BlockBehaviour.Properties properties,
            boolean registerItem) {

        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));

        T block = factory.apply(properties.setId(blockKey));

        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
            Registry.register(BuiltInRegistries.ITEM, itemKey,
                    new BlockItem(block, new Item.Properties()
                            .setId(itemKey)
                            .useBlockDescriptionPrefix()));
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void registerModBlocks() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering tyler2 blocks for " + MutuallyAssuredDestruction.MOD_ID);
    }
}