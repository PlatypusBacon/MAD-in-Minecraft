package bunger.group.moreslots.api;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.item.ItemStack;

public record SlotData(AttachmentTarget target){

    public ItemStack getEquipmentSlot() {
        return target.getAttachedOrElse(SlotTypes.EQUIPMENT_SLOT, ItemStack.EMPTY);
    }

    public void setEquipmentSlot(ItemStack stack) {
        target.setAttached(SlotTypes.EQUIPMENT_SLOT, stack);
    }

    public boolean isEmpty() {
        return getEquipmentSlot().isEmpty();
    }

}
