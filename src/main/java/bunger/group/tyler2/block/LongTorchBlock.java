package bunger.group.tyler2.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Floor-standing long torch.
 *
 * Light bias: VERTICAL = 14 (strong), HORIZONTAL = 10 (weak).
 *
 *
 * Shape: narrower than vanilla (2px vs 4px), taller (14px vs 10px).
 */
public class LongTorchBlock extends TorchBlock {

    public static final MapCodec<LongTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    PARTICLE_OPTIONS_FIELD.forGetter(b -> b.flameParticle),
                    propertiesCodec()
            ).apply(i, LongTorchBlock::new));

    // Narrow and tall: 2px diameter, 14px height.
    private static final VoxelShape SHAPE = Block.column(2.0, 0.0, 14.0);

    public LongTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(flameParticle, properties);
    }

    @Override
    public MapCodec<? extends TorchBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        double x = (double)pos.getX() + (double)0.5F;
        double y = (double)pos.getY() + 1.5;
        double z = (double)pos.getZ() + (double)0.5F;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
        level.addParticle(this.flameParticle, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world,
                                  BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}