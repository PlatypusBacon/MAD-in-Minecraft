package bunger.group.client.mixin;

import bunger.group.alex.item.Arbalest;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;

@Mixin(AvatarRenderer.class)
public class ArbalestAnimMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void fixArbalestChargeDuration(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity.getUseItem().getItem() instanceof Arbalest) {
            state.maxCrossbowChargeDuration = 400.0F;
        }
    }
}