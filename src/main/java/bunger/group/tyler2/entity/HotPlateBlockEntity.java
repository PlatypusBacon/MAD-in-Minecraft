package bunger.group.tyler2.entity;

import bunger.group.tyler2.block.HotPlateBlock;
import bunger.group.tyler2.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

public class HotPlateBlockEntity extends BlockEntity {

    // Set this in your ModBlockEntities registration
    public static BlockEntityType<HotPlateBlockEntity> TYPE;

    private ItemStack item = ItemStack.EMPTY;
    private int smeltProgress = 0;

    private static final int SMELT_TIME = 200; // 10 seconds, matches furnace

    public HotPlateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOT_PLATE_BE, pos, state);
    }

    // --- Ticker ---

    public static void serverTick(Level level, BlockPos pos,
                                  BlockState state, HotPlateBlockEntity be) {
        if (be.item.isEmpty()) {
            be.smeltProgress = 0;
            return;
        }

        if (HotPlateBlock.isHeated(level, pos) && canSmelt(level, be.item)) {
            be.smeltProgress++;
            if (be.smeltProgress >= SMELT_TIME) {
                be.smeltProgress = 0;
                be.item = getSmeltingResult(level, be.item);
                be.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        } else {
            if (be.smeltProgress > 0) be.smeltProgress--;
        }
    }

    // --- Recipe helpers ---
    private static boolean canSmelt(Level level, ItemStack stack) {
        return getSmeltingRecipe(level, stack).isPresent();
    }

    private static Optional<RecipeHolder<SmeltingRecipe>> getSmeltingRecipe(Level level, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().getRecipeFor(
                    RecipeType.SMELTING,
                    new SingleRecipeInput(stack),
                    serverLevel
            );
        }
        return Optional.empty();
    }

    private static ItemStack getSmeltingResult(Level level, ItemStack stack) {
        return getSmeltingRecipe(level, stack)
                .map(r -> r.value().assemble(
                        new SingleRecipeInput(stack)
                ))
                .orElse(ItemStack.EMPTY);
    }

    // --- Item access ---

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack stack) {
        this.item = stack;
        this.smeltProgress = 0;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // --- Persistence: using ValueInput/ValueOutput per the updated BlockEntity API ---

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!item.isEmpty()) {
            output.store("Item", ItemStack.CODEC, item);
        }
        output.putInt("Progress", smeltProgress);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        if (!item.isEmpty()) {
            tag.store("Item", ItemStack.CODEC, item);
        }
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        item = input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        smeltProgress = input.getIntOr("Progress", 0);
    }
}