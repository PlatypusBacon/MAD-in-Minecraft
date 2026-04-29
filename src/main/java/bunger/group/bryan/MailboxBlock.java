package bunger.group.bryan;

import java.util.function.Function;
import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.Direction;
// import net.minecraft.world.item.CreativeModeTabs;
// import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
// import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




// TODO: add item texture
// TODO: add better in game name

public class MailboxBlock extends Block {


    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public MailboxBlock(Properties properties) {
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
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos,
                                            Player player, BlockHitResult hit) {

        // Find tax book in inventory
        ItemStack taxBook = ItemStack.EMPTY;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.has(MutuallyAssuredDestruction.TAX_ITEMS)) {
                taxBook = stack;
                break;
            }
        }

        if (taxBook.isEmpty()){
            return InteractionResult.PASS;
        }

        String data = taxBook.get(MutuallyAssuredDestruction.TAX_ITEMS);
        if (data == null || data.isEmpty()){ 
            return InteractionResult.PASS;
        }

        List<String> required = new ArrayList<>(Arrays.asList(data.split(",")));
        
        // Get held item
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return InteractionResult.PASS;

        String heldId = BuiltInRegistries.ITEM.getKey(held.getItem()).toString();

        // Check if it's required
        if (required.contains(heldId)) {

            held.shrink(1); // delete item

            required.remove(heldId);

            String newData = String.join(",", required);
            taxBook.set(MutuallyAssuredDestruction.TAX_ITEMS, newData);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
