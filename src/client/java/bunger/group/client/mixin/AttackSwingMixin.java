
package bunger.group.client.mixin;

import bunger.group.tyler.data.BearBoxersSwingData;
import bunger.group.tyler.item.ModItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class AttackSwingMixin {

    private static boolean redirecting = false;

    @Inject(
            method = "swing(Lnet/minecraft/world/InteractionHand;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSwing(InteractionHand hand, CallbackInfo ci) {
        if (redirecting) return;

        LocalPlayer self = (LocalPlayer)(Object)this;
        if (!self.getMainHandItem().is(ModItems.BEAR_BOXERS)) return;
        if (hand != InteractionHand.MAIN_HAND) return;

        ci.cancel();

        boolean useOffhand = BearBoxersSwingData.PUNCH_SIDE_NEXT.getOrDefault(self.getUUID(), false);
        BearBoxersSwingData.PUNCH_SIDE_NEXT.put(self.getUUID(), !useOffhand);
        BearBoxersSwingData.PUNCH_SIDE_CURRENT.put(self.getUUID(), useOffhand);

        InteractionHand swingHand = useOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;

        redirecting = true;
        self.swing(swingHand, true);
        redirecting = false;
    }
}