package bunger.group.tyler2.entity;

import bunger.group.tyler2.block.ModBlockEntities;
import bunger.group.tyler2.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TanningRackBlockEntity extends BlockEntity {

    public static BlockEntityType<TanningRackBlockEntity> TYPE;

    public static final int SLOT_COUNT = 3;

    /**
     * How many ticks of valid sunlight are needed to dry one item.
     * 24000 ticks = 1 Minecraft day; 6000 = 5 minutes of daytime exposure.
     */
    private static final int DRY_TIME = 20;

    private final ItemStack[] items   = new ItemStack[SLOT_COUNT];
    private final int[]       progress = new int[SLOT_COUNT];

    public TanningRackBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TANNING_RACK_BE, pos, state);
        for (int i = 0; i < SLOT_COUNT; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    // -------------------------------------------------------------------------
    // Ticker
    // -------------------------------------------------------------------------

    public static void serverTick(Level level, BlockPos pos,
                                  BlockState state, TanningRackBlockEntity be) {

        if (!isSunlit(level, pos)) {
            // Pause — do not reset progress so clouds don't ruin a nearly-dried item
            return;
        }

        boolean anyChanged = false;

        for (int i = 0; i < SLOT_COUNT; i++) {
            ItemStack stack = be.items[i];
            if (stack.isEmpty()) {
                be.progress[i] = 0;
                continue;
            }

            ItemStack result = getDryingResult(stack);
            if (result.isEmpty()) continue; // item in slot has no recipe

            be.progress[i]++;
            if (be.progress[i] >= DRY_TIME) {
                be.progress[i] = 0;
                be.items[i] = result;
                anyChanged = true;
            }
        }

        if (anyChanged) {
            be.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    /**
     * Returns true when the block can see the sky, it is daytime, and it is
     * not currently raining or thundering.
     */
    public static boolean isSunlit(Level level, BlockPos pos) {
        if (level.isRaining() || level.isThundering()) return false;
        // getOverworldClockTime() is the 1.21 replacement for getDayTime().
        // Daytime runs roughly 0–12000 within each 24000-tick cycle.
        long time = level.getOverworldClockTime() % 24000;
        if (time >= 12000) return false;
        return level.canSeeSky(pos.above());
    }

    // -------------------------------------------------------------------------
    // Drying recipes
    // Replace these mappings with your own items/tags once registered.
    // -------------------------------------------------------------------------

    /**
     * Maps an input ItemStack to its dried output, or ItemStack.EMPTY if the
     * item cannot be dried on the tanning rack.
     *
     * Add your own mod items here (e.g. ModItems.RAW_MEAT -> ModItems.DRIED_MEAT,
     * ModItems.ANIMAL_SKIN -> Items.LEATHER, etc.).
     */
    public static ItemStack getDryingResult(ItemStack input) {
        if (input.isEmpty()) return ItemStack.EMPTY;


        if (input.is(ModItems.ANIMAL_SKIN)) {
            return new ItemStack(Items.LEATHER);
        }

        if (input.is(Items.BEEF)) {
            return new ItemStack(ModItems.BEEF_JERKY);
        }
        if (input.is(Items.PORKCHOP)) {
            return new ItemStack(ModItems.PORK_JERKY);
        }
        if (input.is(Items.CHICKEN)) {
            return new ItemStack(ModItems.CHICKEN_JERKY);
        }
        if (input.is(Items.MUTTON)) {
            return new ItemStack(ModItems.SHEEP_JERKY);
        }
        if (input.is(Items.SALMON)) {
            return new ItemStack(ModItems.DRIED_SALMON);
        }
        if (input.is(Items.COD)) {
            return new ItemStack(ModItems.DRIED_COD);
        }

        return ItemStack.EMPTY;
    }

    /** Returns true if this item can be placed on the rack. */
    public static boolean isValidInput(ItemStack stack) {
        return !getDryingResult(stack).isEmpty();
    }

    // -------------------------------------------------------------------------
    // Slot access (used by the Block)
    // -------------------------------------------------------------------------

    public ItemStack getItem(int slot) {
        return items[slot];
    }

    public void setItem(int slot, ItemStack stack) {
        items[slot] = stack;
        progress[slot] = 0;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    /** How far along slot {@code slot} is, as a value 0.0–1.0. */
    public float getProgress(int slot) {
        if (items[slot].isEmpty()) return 0f;
        return (float) progress[slot] / DRY_TIME;
    }

    // -------------------------------------------------------------------------
    // Persistence  (ValueInput / ValueOutput — matches HotPlateBlockEntity)
    // -------------------------------------------------------------------------

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                output.store("Item" + i, ItemStack.CODEC, items[i]);
            }
            output.putInt("Progress" + i, progress[i]);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        for (int i = 0; i < SLOT_COUNT; i++) {
            items[i]   = input.read("Item" + i, ItemStack.CODEC).orElse(ItemStack.EMPTY);
            progress[i] = input.getIntOr("Progress" + i, 0);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!items[i].isEmpty()) {
                tag.store("Item" + i, ItemStack.CODEC, items[i]);
            }
        }
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}