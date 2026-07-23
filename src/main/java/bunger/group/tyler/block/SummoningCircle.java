package bunger.group.tyler.block;
import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class SummoningCircle extends Block {

    // very thin — just 1px tall, no collision
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public SummoningCircle(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world,
                                        BlockPos pos, CollisionContext context) {
        return Shapes.empty(); // no hitbox — players walk through/over it
    }

    public static Block registerBlock(String name, Function<Properties, Block> factory, BlockBehaviour.Properties properties, boolean registerItem) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        Block block = factory.apply(properties.setId(blockKey));
        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
            Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()));
        }
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }
}


