package bunger.group.tyler2.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.List;

public class BigPlankBlock extends Block {

    public static final EnumProperty<DoubleBlockHalf> HALF =
            BlockStateProperties.DOUBLE_BLOCK_HALF; // top/bottom for vertical
    // For 2x2 horizontal, define your own:
    public static final EnumProperty<Half> EAST_HALF =
            EnumProperty.create("east_half", Half.class);
    public static final EnumProperty<Half> NORTH_HALF =
            EnumProperty.create("north_half", Half.class);

    public enum Half implements StringRepresentable {
        NEAR, FAR;
        public String getSerializedName() { return name().toLowerCase(); }
    }

    public BigPlankBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(EAST_HALF, Half.NEAR)
                .setValue(NORTH_HALF, Half.NEAR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EAST_HALF, NORTH_HALF);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        // Place origin block, then fill in the other 3
        BlockPos pos = ctx.getClickedPos();
        Level level = ctx.getLevel();

        // Check all 4 positions are free
        if (level.getBlockState(pos.east()).canBeReplaced() &&
                level.getBlockState(pos.north()).canBeReplaced() &&
                level.getBlockState(pos.east().north()).canBeReplaced()) {

            // Place the other 3 quadrants
            level.setBlock(pos.east(),       defaultBlockState().setValue(EAST_HALF, Half.FAR).setValue(NORTH_HALF, Half.NEAR), 3);
            level.setBlock(pos.north(),      defaultBlockState().setValue(EAST_HALF, Half.NEAR).setValue(NORTH_HALF, Half.FAR), 3);
            level.setBlock(pos.east().north(),defaultBlockState().setValue(EAST_HALF, Half.FAR).setValue(NORTH_HALF, Half.FAR), 3);

            return defaultBlockState()
                    .setValue(EAST_HALF, Half.NEAR)
                    .setValue(NORTH_HALF, Half.NEAR);
        }
        return null; // can't place
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        // Remove the other 3 when any quadrant is broken
        if (!level.isClientSide()) {
            BlockPos origin = getOrigin(pos, state);
            for (BlockPos quad : getAll4(origin)) {
                if (!quad.equals(pos)) {
                    level.setBlock(quad, Blocks.AIR.defaultBlockState(), 35);
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    private BlockPos getOrigin(BlockPos pos, BlockState state) {
        BlockPos p = pos;
        if (state.getValue(EAST_HALF) == Half.FAR)  p = p.west();
        if (state.getValue(NORTH_HALF) == Half.FAR) p = p.south();
        return p;
    }

    private List<BlockPos> getAll4(BlockPos origin) {
        return List.of(origin, origin.east(), origin.north(), origin.east().north());
    }
}
