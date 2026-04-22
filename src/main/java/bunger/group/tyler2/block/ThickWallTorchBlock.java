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
 * Wall-mounted thick torch.
 *
 * Light bias: HORIZONTAL = 14 (strong), VERTICAL = 10 (weak).
 * Light level is controlled by the lightLevel lambda in ModBlocks.torchPropsThickWall().
 *
 * Shape: wider than vanilla (8px vs 6px), same protrusion depth.
 * Vanilla uses: Shapes.rotateHorizontal(Block.boxZ(5, 3, 13, 11, 16))
 * We widen X from 5–11 to 4–12 (8px) for the thick profile.
 */
public class ThickWallTorchBlock extends WallTorchBlock {

    // Typed as MapCodec<ThickWallTorchBlock> so the forGetter lambda correctly
    // sees `b` as ThickWallTorchBlock and can access b.flameParticle.
    // The unchecked cast happens only at the codec() return site to satisfy
    // WallTorchBlock.codec()'s non-wildcard MapCodec<WallTorchBlock> return type.
    public static final MapCodec<ThickWallTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    PARTICLE_OPTIONS_FIELD.forGetter(b -> b.flameParticle),
                    propertiesCodec()
            ).apply(i, ThickWallTorchBlock::new));

    private static final Map<net.minecraft.core.Direction, VoxelShape> SHAPES =
            Shapes.rotateHorizontal(Block.boxZ(4.0, 3.0, 11.0, 12.0, 16.0));

    public ThickWallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(flameParticle, properties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MapCodec<WallTorchBlock> codec() {
        // Safe: the codec constructs ThickWallTorchBlock instances, which are WallTorchBlocks.
        return (MapCodec<WallTorchBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world,
                                  BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
}