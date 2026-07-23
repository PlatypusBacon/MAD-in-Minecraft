package bunger.group.tyler2.block;

import bunger.group.tyler2.entity.HotPlateBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class HotPlateBlock extends BaseEntityBlock {

    public static final MapCodec<HotPlateBlock> CODEC = simpleCodec(HotPlateBlock::new);

    public HotPlateBlock(Properties properties) {
        super(properties);
    }
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 3, 16);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public static boolean isHeated(Level level, BlockPos pos) {
        BlockPos below = pos.below();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockState state = level.getBlockState(below.offset(x, 0, z));
                if (state.is(Blocks.FIRE)
                        || state.is(Blocks.SOUL_FIRE)
                        || state.is(Blocks.CAMPFIRE)
                        || state.is(Blocks.SOUL_CAMPFIRE)
                        || state.is(Blocks.LAVA)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level,
                                               BlockPos pos, Player player, BlockHitResult hit) {

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof HotPlateBlockEntity be) {
            ItemStack held = player.getMainHandItem();

            if (!held.isEmpty() && be.getItem().isEmpty()) {
                // Place item onto the plate
                be.setItem(held.copyWithCount(1));
                held.shrink(1);
                return InteractionResult.CONSUME;
            } else if (held.isEmpty() && !be.getItem().isEmpty()) {
                // Pick up the item from the plate
                player.addItem(be.getItem());
                be.setItem(ItemStack.EMPTY);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }



    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HotPlateBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        return createTickerHelper(type,
                ModBlockEntities.HOT_PLATE_BE,
                HotPlateBlockEntity::serverTick);
    }
}