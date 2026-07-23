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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;


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
    protected InteractionResult useItemOn(ItemStack held,
                                          BlockState state,
                                          Level world,
                                          BlockPos pos,
                                          Player player,
                                          InteractionHand hand,
                                          BlockHitResult hit) {

        TaxData data = TaxLogic.PLAYER_TAXES.get(player.getUUID());

        if (data == null) {
            return InteractionResult.PASS;
        }

        if (held.isEmpty()) {
            return InteractionResult.PASS;
        }

        String heldId = BuiltInRegistries.ITEM.getKey(held.getItem()).toString();

        if (data.requiredItems.containsKey(heldId)) {

            int remaining = data.requiredItems.get(heldId) - 1;

            held.shrink(1);

            if (remaining <= 0) {
                data.requiredItems.remove(heldId);
            } else {
                data.requiredItems.put(heldId, remaining);
            }

            if (data.requiredItems.isEmpty()) {
                data.paid = true;
                player.sendSystemMessage(Component.literal("Taxes paid."));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }


    // @Override
    // public void playerDestroy(Level level,
    //                         Player player,
    //                         BlockPos pos,
    //                         BlockState state,
    //                         BlockEntity blockEntity,
    //                         ItemStack tool) {

    //     if (!level.isClientSide()) {

    //         Item item = tool.getItem();

    //         boolean correctTool =
    //                 item == Items.COPPER_PICKAXE ||
    //                 item == Items.IRON_PICKAXE ||
    //                 item == Items.GOLDEN_PICKAXE ||
    //                 item == Items.DIAMOND_PICKAXE ||
    //                 item == Items.NETHERITE_PICKAXE;

    //         if (correctTool) {
    //             popResource(level, pos, new ItemStack(this));
    //         }
    //     }

    //     super.playerDestroy(level, player, pos, state, blockEntity, tool);
    // }

    // @Override
    // protected boolean canDropFromExplosion(Explosion explosion) {
    //     return false;
    // }
}
