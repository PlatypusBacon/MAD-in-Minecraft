package bunger.group.moreslots.api;

import bunger.group.moreslots.api.SlotTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ExtraEquipmentSlot extends Slot {

    private final Player player;

    public ExtraEquipmentSlot(Player player, int x, int y) {
        super(player.getInventory(), -1, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return true;
        //return stack.getItem() instanceof ExtraEquipmentItem;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

       @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ItemStack getItem() {
        ItemStack stack = player.getAttachedOrElse(SlotTypes.EQUIPMENT_SLOT, ItemStack.EMPTY);
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    public void set(ItemStack stack) {
        player.setAttached(SlotTypes.EQUIPMENT_SLOT, stack.copy());
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItem().copy();
        player.setAttached(SlotTypes.EQUIPMENT_SLOT, ItemStack.EMPTY);
        this.setChanged();
        return current;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        this.setChanged();
    }

}