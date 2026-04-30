package bunger.group.ethan;


import java.util.function.Function;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;



public class AltarFragmentBlock extends Block {


    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AltarFragmentBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }





    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties, boolean registerItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        Block block = factory.apply(properties.setId(blockKey));
        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
            Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        }
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        checkForAltar(level, pos);
    }

    private void checkForAltar(Level level, BlockPos pos) {
        int[][] offsets = {{0,0}, {-1,0}, {0,-1}, {-1,-1}};
        for (int[] offset : offsets) {
            BlockPos corner = pos.offset(offset[0], 0, offset[1]);
            if (isAltarSquare(level, corner)) {
                formAltar(level, corner);
                return;
            }
        }
    }

    private boolean isAltarSquare(Level level, BlockPos corner) {
        return level.getBlockState(corner).getBlock() instanceof AltarFragmentBlock &&
            level.getBlockState(corner.offset(1, 0, 0)).getBlock() instanceof AltarFragmentBlock &&
            level.getBlockState(corner.offset(0, 0, 1)).getBlock() instanceof AltarFragmentBlock &&
            level.getBlockState(corner.offset(1, 0, 1)).getBlock() instanceof AltarFragmentBlock;
    }

    private void formAltar(Level level, BlockPos corner) {
        BlockPos[] positions = {
            corner,
            corner.offset(1, 0, 0),
            corner.offset(0, 0, 1),
            corner.offset(1, 0, 1)
        };
        for (BlockPos altarPos : positions) {
            Direction facing = level.getBlockState(altarPos).getValue(FACING);
            level.setBlock(altarPos, 
                MutuallyAssuredDestruction.ALTAR.defaultBlockState().setValue(AltarBlock.FACING, facing), 
                3);
        }
        level.playSound(
            null,
            corner.offset(1, 0, 1),
            MutuallyAssuredDestruction.ALTAR_FORM,
            SoundSource.BLOCKS,
            0.3F,
            1.0F
        );
        // for (ServerPlayer player : .players()) {
        //     player.sendOverlayMessage(Component.literal(""));
        // }
        
    }




    
}
