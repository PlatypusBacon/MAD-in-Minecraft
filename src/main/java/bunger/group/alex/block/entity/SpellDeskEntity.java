package bunger.group.alex.block.entity;

import bunger.group.alex.container.ImplementedContainer;
import bunger.group.alex.item.ModItems;
import bunger.group.alex.menu.SpellDeskMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class SpellDeskEntity extends BlockEntity implements ImplementedContainer, MenuProvider {

    public static final int CONTAINER_SIZE = 2;
    public static final int MAX_MANA = 100;

    public static final int BLANK_SCROLL_SLOT = 0;
    public static final int PURE_MANA_SLOT = 1;

    private final NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private int storedMana = 0;

    public SpellDeskEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.SPELL_DESK_ENTITY, worldPosition, blockState);
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        ItemStack manaStack = items.get(PURE_MANA_SLOT);
        if (!manaStack.isEmpty() && manaStack.is(ModItems.PURE_MANA) && storedMana < MAX_MANA) {
            storedMana++;
            manaStack.shrink(1);
            setChanged();
        }
    }

    public int getStoredMana() { return storedMana; }

    public void setStoredMana(int mana) { storedMana = mana; }

    public boolean drainMana(int amount) {
        if (storedMana >= amount) {
            storedMana -= amount;
            setChanged();
            return true;
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getItems() { return items; }

    @Override
    @NonNull
    public Component getDisplayName() {
        return Component.translatable("block.mutually-assured-destruction.spell_desk");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SpellDeskMenu(containerId, inventory, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        storedMana = input.getIntOr("StoredMana", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
        output.putInt("StoredMana", storedMana);
        super.saveAdditional(output);
    }
}