package bunger.group.tyler2.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

/**
 * Wall-mounted long torch.
 *
 * Light bias: VERTICAL = 14 (strong), HORIZONTAL = 10 (weak).
 * Wall-mounted against its preferred vertical axis, so emits 10.
 * Light level is controlled by the lightLevel lambda in ModBlocks.torchPropsLongWall().
 *
 * Shape: tall and narrow, protruding slightly from the wall.
 * Vanilla uses: Shapes.rotateHorizontal(Block.boxZ(5, 3, 13, 11, 16))
 * We narrow X from 5–11 to 6–10 (4px) and extend height from 3–13 to 2–14.
 */
public class LongWallTorchBlock extends WallTorchBlock {

    // Typed as MapCodec<LongWallTorchBlock> so the forGetter lambda correctly
    // sees `b` as LongWallTorchBlock and can access b.flameParticle.
    // The unchecked cast happens only at the codec() return site to satisfy
    // WallTorchBlock.codec()'s non-wildcard MapCodec<WallTorchBlock> return type.
    public static final MapCodec<LongWallTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    PARTICLE_OPTIONS_FIELD.forGetter(b -> b.flameParticle),
                    propertiesCodec()
            ).apply(i, LongWallTorchBlock::new));

    private static final Map<net.minecraft.core.Direction, VoxelShape> SHAPES =
            Shapes.rotateHorizontal(Block.boxZ(6.0, 2.0, 10.0, 14.0, 16.0));

    public LongWallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(flameParticle, properties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapCodec<WallTorchBlock> codec() {
        // Safe: the codec constructs LongWallTorchBlock instances, which are WallTorchBlocks.
        return (MapCodec<WallTorchBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world,
                                  BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
}