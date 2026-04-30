package bunger.group.tyler3.block;

import bunger.group.MutuallyAssuredDestruction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class PlatformTrackBlock extends BaseRailBlock {

    public static final MapCodec<PlatformTrackBlock> CODEC = simpleCodec(PlatformTrackBlock::new);

    public static final EnumProperty<RailShape> SHAPE = EnumProperty.create(
            "shape", RailShape.class,
            RailShape.NORTH_SOUTH,
            RailShape.EAST_WEST,
            RailShape.NORTH_EAST,
            RailShape.NORTH_WEST,
            RailShape.SOUTH_EAST,
            RailShape.SOUTH_WEST
    );

    // Thin flat plane at y=7 to y=9 — centred in block
    private static final VoxelShape RAIL_SHAPE = Block.box(0, 7, 0, 16, 9, 16);

    public PlatformTrackBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(SHAPE, RailShape.NORTH_SOUTH)
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseRailBlock> codec() {
        return CODEC;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        return RAIL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean canSurvive(BlockState state,
                                 net.minecraft.world.level.LevelReader level, BlockPos pos) {
        return true; // floats freely
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos,
                                   Block block,
                                   net.minecraft.world.level.redstone.Orientation orientation,
                                   boolean movedByPiston) {
        // Do not remove on neighbour change — floats freely
        if (!level.isClientSide() && level.getBlockState(pos).is(this)) {
            this.updateState(state, level, pos, block);
        }
    }

    @Override
    protected BlockState updateDir(Level level, BlockPos pos,
                                   BlockState state, boolean first) {
        if (level.isClientSide()) return state;
        RailShape current = state.getValue(SHAPE);
        BlockState updated = new RailState(level, pos, state)
                .place(false, first, current).getState();
        RailShape newShape = updated.getValue(SHAPE);
        // Strip ascending slopes — only flat shapes supported
        if (newShape.isSlope()) {
            updated = updated.setValue(SHAPE, RailShape.NORTH_SOUTH);
        }
        return updated;
    }

    public static Block registerBlock(String name,
                                      Function<BlockBehaviour.Properties, Block> factory,
                                      BlockBehaviour.Properties properties,
                                      boolean registerItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name)
        );
        Block block = factory.apply(properties.setId(blockKey));

        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name)
            );
            Registry.register(BuiltInRegistries.ITEM, itemKey,
                    new BlockItem(block,
                            new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }
}