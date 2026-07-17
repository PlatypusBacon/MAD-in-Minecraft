package bunger.group.tyler2.block;

import bunger.group.tyler2.entity.TanningRackBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class TanningRackBlock extends BaseEntityBlock {


    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final MapCodec<TanningRackBlock> CODEC = simpleCodec(TanningRackBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // -------------------------------------------------------------------------
    // Shape
    // -------------------------------------------------------------------------

    private static final VoxelShape SHAPE_BAR   = Block.box(1,  12, 7, 15, 14,  9);
    private static final VoxelShape SHAPE_LEG_L  = Block.box(1,  0,  5,  4, 12, 11);
    private static final VoxelShape SHAPE_LEG_R  = Block.box(12, 0,  5, 15, 12, 11);
    private static final VoxelShape SHAPE = Shapes.or(SHAPE_BAR, SHAPE_LEG_L, SHAPE_LEG_R);
    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(
            Block.box(1, 12, 7, 15, 14, 9),  // bar along X
            Block.box(1, 0, 5, 3, 12, 11),
            Block.box(12, 0, 5, 14, 12, 11)
    );

    private static final VoxelShape SHAPE_EAST_WEST = Shapes.or(
            Block.box(7, 12, 1, 9, 14, 15),  // bar along Z
            Block.box(5, 0, 1, 11, 12, 3),
            Block.box(5, 0, 12, 11, 12, 14)
    );

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level,
                                  BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return (facing == Direction.NORTH || facing == Direction.SOUTH)
                ? SHAPE_NORTH_SOUTH
                : SHAPE_EAST_WEST;
    }
    // -------------------------------------------------------------------------
    // Constructor / state setup
    // -------------------------------------------------------------------------

    public TanningRackBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    // -------------------------------------------------------------------------
    // Shape & render
    // -------------------------------------------------------------------------


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // -------------------------------------------------------------------------
    // Slot hit detection
    //
    // Maps the hit location's local X or Z coordinate (depending on facing)
    // onto one of the 6 hanging slots.
    // -------------------------------------------------------------------------

    private int getClickedSlot(BlockState state, BlockHitResult hit) {
        Vec3 loc = hit.getLocation();
        double localX = loc.x - Math.floor(loc.x);
        double localZ = loc.z - Math.floor(loc.z);
        double localY = loc.y - Math.floor(loc.y);

        // Only register clicks in the hanging zone (below bar top, above ground)
        if (localY > 0.9 || localY < 0.0) return -1;

        Direction facing = state.getValue(FACING);
        // Bar runs perpendicular to facing:
        //   NORTH/SOUTH → bar along X axis
        //   EAST/WEST   → bar along Z axis
        double t;
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            t = (localX - 0.0625) / 0.875; // 1px inset on each side of 16px block
        } else {
            t = (localZ - 0.0625) / 0.875;
        }

        if (t < 0 || t > 1) return -1;
        // Clamp to avoid an out-of-bounds index on exactly t == 1.0
        return Math.min((int)(t * TanningRackBlockEntity.SLOT_COUNT),
                TanningRackBlockEntity.SLOT_COUNT - 1);
    }

    // -------------------------------------------------------------------------
    // Interaction
    // -------------------------------------------------------------------------

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
                                               BlockPos pos, Player player,
                                               BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (!(level.getBlockEntity(pos) instanceof TanningRackBlockEntity be))
            return InteractionResult.PASS;

        int slot = getClickedSlot(state, hit);
        if (slot < 0) return InteractionResult.PASS;

        ItemStack held   = player.getMainHandItem();
        ItemStack inSlot = be.getItem(slot);

        if (!held.isEmpty() && inSlot.isEmpty()) {
            if (!TanningRackBlockEntity.isValidInput(held)) return InteractionResult.PASS;
            be.setItem(slot, held.copyWithCount(1));
            held.shrink(1);
            return InteractionResult.CONSUME;
        } else if (held.isEmpty() && !inSlot.isEmpty()) {
            player.addItem(inSlot.copy());
            be.setItem(slot, ItemStack.EMPTY);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    // -------------------------------------------------------------------------
    // Drop contents when broken
    // -------------------------------------------------------------------------

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof TanningRackBlockEntity be) {
            for (int i = 0; i < TanningRackBlockEntity.SLOT_COUNT; i++) {
                ItemStack stack = be.getItem(i);
                if (!stack.isEmpty()) Block.popResource(level, pos, stack);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    // -------------------------------------------------------------------------
    // Block entity wiring
    // -------------------------------------------------------------------------

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TanningRackBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type,
                ModBlockEntities.TANNING_RACK_BE,
                TanningRackBlockEntity::serverTick);
    }
}