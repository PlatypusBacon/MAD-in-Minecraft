package bunger.group.csmit863.block;

import bunger.group.csmit863.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MagicMushroomBlock extends BushBlock {
    public static final IntegerProperty GROWTH = IntegerProperty.create("growth", 0, 4);
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    public MagicMushroomBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(GROWTH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GROWTH);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.isSolidRender();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int growth = state.getValue(GROWTH);
        grow(state, level, pos, growth);


    }

    private void grow(BlockState state, ServerLevel level, BlockPos pos, int growth) {
        if (growth >= 4) {

            // convert center block
            level.setBlock(pos, ModBlocks.HALLUCINITE_BLOCK.defaultBlockState(), 3);
            level.sendParticles(
                    ParticleTypes.SPORE_BLOSSOM_AIR,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    50,
                    2.0, 1.0, 2.0,
                    0.01
            );
            int radius = 4;
            int r2 = radius * radius;

            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {

                        if (x * x + y * y + z * z > r2) continue; // 👈 SPHERE MASK

                        mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);

                        BlockState stateAt = level.getBlockState(mutable);

                        // Only replace natural ground blocks
                        if (stateAt.is(Blocks.GRASS_BLOCK) ||
                                stateAt.is(Blocks.DIRT) ||
                                stateAt.is(Blocks.COARSE_DIRT)) {

                            level.setBlock(mutable, Blocks.MYCELIUM.defaultBlockState(), 3);
                        }
                    }
                }
            }

        } else {
            level.setBlock(pos, state.setValue(GROWTH, growth + 1), 3);
        }
    }



    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(GROWTH) < 4;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        grow(state, level, pos, state.getValue(GROWTH));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3f) { // adjust density
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

            level.addParticle(
                    ParticleTypes.SOUL, // or CAMPFIRE_COSY_SMOKE
                    x, y, z,
                    0.0, 0.05, 0.0
            );
            level.addParticle(
                    ParticleTypes.ENCHANT, // or CAMPFIRE_COSY_SMOKE
                    x, y, z,
                    0.0, 0.05, 0.0
            );
        }
    }
}