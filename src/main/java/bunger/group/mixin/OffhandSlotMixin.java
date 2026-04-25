package bunger.group.mixin;

import bunger.group.tyler.item.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class OffhandSlotMixin {

    @Inject(method = "setItem", at = @At("HEAD"), cancellable = true)
    private void preventOffhandOverwrite(int slot, ItemStack stack, CallbackInfo ci) {
        if (slot != Inventory.SLOT_OFFHAND) return; // 40
        if (stack.is(ModItems.BEAR_BOXERS)) return;  // allow our ghost copy
        if (stack.isEmpty()) return;                  // allow clearing

        Inventory self = (Inventory)(Object)this;
        if (self.getSelectedItem().is(ModItems.BEAR_BOXERS)) {
            ci.cancel();
        }
    }
}