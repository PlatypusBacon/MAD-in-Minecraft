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
 * Floor-standing thick torch.
 *
 * Light bias: HORIZONTAL = 14 (strong), VERTICAL = 10 (weak).
 *
 * Light level is controlled by the {@code lightLevel} lambda passed into
 * {@link BlockBehaviour.Properties} in {@link ModBlocks} — it is baked into
 * the blockstate cache at construction time and cannot be overridden on the
 * block class. The floor variant always emits 14 since it has no FACING state.
 *
 * Shape: wider than vanilla (6px vs 4px diameter), same height (10px).
 */
public class ThickTorchBlock extends TorchBlock {

    public static final MapCodec<ThickTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    PARTICLE_OPTIONS_FIELD.forGetter(b -> b.flameParticle),
                    propertiesCodec()
            ).apply(i, ThickTorchBlock::new));

    // Wider than vanilla (vanilla uses column(4, 0, 10)).
    // 6px diameter, 10px tall — short and wide reinforces "thick" feel.
    private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 10.0);

    public ThickTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(flameParticle, properties);
    }
    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        double x = (double)pos.getX() + (double)0.5F;
        double y = (double)pos.getY() + 0.25;
        double z = (double)pos.getZ() + (double)0.5F;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
        level.addParticle(this.flameParticle, x, y, z, (double)0.0F, (double)0.0F, (double)0.0F);
    }

    @Override
    public MapCodec<? extends TorchBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world,
                                  BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}