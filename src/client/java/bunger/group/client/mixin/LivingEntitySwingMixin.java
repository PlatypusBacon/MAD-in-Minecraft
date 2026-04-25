package bunger.group.client.mixin;

import bunger.group.tyler.data.BearBoxersSwingData;
import bunger.group.tyler.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntitySwingMixin {

    @Inject(
            method = "swing(Lnet/minecraft/world/InteractionHand;Z)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSwingTwoArg(InteractionHand hand, boolean sendToSwingingEntity, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (!self.getMainHandItem().is(ModItems.BEAR_BOXERS)) return;

        // Let the correct hand through, cancel incorrect main hand swings
        boolean offhandActive = BearBoxersSwingData.PUNCH_SIDE_CURRENT.getOrDefault(
                self.getUUID(), false
        );
        InteractionHand expectedHand = offhandActive ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;

        if (hand != expectedHand) {
            ci.cancel();
        }
        // If hand matches, let vanilla proceed — it will set swingingArm correctly
    }
}