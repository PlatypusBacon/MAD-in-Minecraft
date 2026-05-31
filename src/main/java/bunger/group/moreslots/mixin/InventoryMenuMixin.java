package bunger.group.moreslots.mixin;

import bunger.group.moreslots.api.ExtraEquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {

    protected InventoryMenuMixin(MenuType<?> type, int i) {
        super(type, i);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addExtraSlot(Inventory playerInventory, boolean active, Player player, CallbackInfo ci) {
        // x: -(PANEL_SIZE=24) + (PADDING=3) = -21
        // y: ARMOR_OFFSET_Y(8) + PADDING(3) = 11
        this.addSlot(new ExtraEquipmentSlot(player, -20, 8));
    }
}