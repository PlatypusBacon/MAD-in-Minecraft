package bunger.group.tyler2.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        Direction direction = (Direction)state.getValue(FACING);
        double x = (double)pos.getX() + (double)0.5F;
        double y = (double)pos.getY() + 0.3;
        double z = (double)pos.getZ() + (double)0.5F;
        double h = 0.22;
        double r = 0.27;
        Direction opposite = direction.getOpposite();
        level.addParticle(ParticleTypes.SMOKE, x + 0.27 * (double)opposite.getStepX(), y + 0.22, z + 0.27 * (double)opposite.getStepZ(), (double)0.0F, (double)0.0F, (double)0.0F);
        level.addParticle(this.flameParticle, x + 0.27 * (double)opposite.getStepX(), y + 0.22, z + 0.27 * (double)opposite.getStepZ(), (double)0.0F, (double)0.0F, (double)0.0F);
    }

}