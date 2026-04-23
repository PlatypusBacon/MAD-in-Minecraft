package bunger.group.alex.block;

import bunger.group.alex.block.entity.ModBlockEntities;
import bunger.group.alex.block.entity.SpellDeskEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.particles.ParticleTypes;

public class SpellDesk extends BaseEntityBlock {

    public static final EnumProperty<Direction> FACING;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }

    public SpellDesk(Properties settings) {
        super(settings);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SpellDesk::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction facing = state.getValue(FACING);
        double[][] candles = getCandlePositions(facing, pos);

        for (double[] candle : candles) {
            // Only spawn particles ~40% of the time per candle per tick
            if (random.nextInt(10) > 3) continue;

            double x = candle[0] + (random.nextDouble() - 0.5) * 0.05;
            double y = candle[1];
            double z = candle[2] + (random.nextDouble() - 0.5) * 0.05;

            level.addParticle(ParticleTypes.SMALL_FLAME, x, y, z, 0, 0, 0);

            // Smoke only 1 in 20 ticks
            if (random.nextInt(20) == 0) {
                level.addParticle(ParticleTypes.SMOKE, x, y + 0.05, z, 0, 0.01, 0);
            }
        }
    }

    private double[][] getCandlePositions(Direction facing, BlockPos pos) {
        double[][] base = {
                {13.5 / 16.0, 20.0 / 16.0, 13.5 / 16.0},
                {13.5 / 16.0, 22.0 / 16.0, 14.5 / 16.0},
                {14.5 / 16.0, 21.0 / 16.0, 14.5 / 16.0},
                { 0.5 / 16.0, 20.0 / 16.0, 11.5 / 16.0},
                { 4.5 / 16.0, 21.0 / 16.0, 15.5 / 16.0},
        };

        double[][] world = new double[base.length][3];
        for (int i = 0; i < base.length; i++) {
            double[] rotated = rotateForFacing(base[i][0], base[i][2], facing);
            world[i][0] = pos.getX() + rotated[0];
            world[i][1] = pos.getY() + base[i][1];
            world[i][2] = pos.getZ() + rotated[1];
        }
        return world;
    }

    private double[] rotateForFacing(double x, double z, Direction facing) {
        return switch (facing) {
            case NORTH -> new double[]{x, z};
            case SOUTH -> new double[]{1 - x, 1 - z};
            case WEST  -> new double[]{z, 1 - x};
            case EAST  -> new double[]{1 - z, x};
            default    -> new double[]{x, z};
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof SpellDeskEntity spellDesk) {
            player.openMenu(spellDesk);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type, ModBlockEntities.SPELL_DESK_ENTITY,
                (world, blockPos, blockState, entity) -> entity.tick());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpellDeskEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}