package bunger.group.client.mixin;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.mixin.accessor.GameRendererAccessor;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class HallucinationClientMixin {
    private boolean shaderLoaded = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        var effect = player.getEffect(ModItems.HALLUCINATION_EFFECT);
        if (effect == null) {
            if (shaderLoaded) {
                mc.gameRenderer.clearPostEffect();
                shaderLoaded = false;
            }
            return;
        }

        if (!shaderLoaded) {
            GameRendererAccessor accessor = (GameRendererAccessor) mc.gameRenderer;
            accessor.invokeSetPostEffect(
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "hallucination")
            );
            shaderLoaded = true;
        }

        int amplifier = effect.getAmplifier();
        if (player.getRandom().nextFloat() < 0.004f * (amplifier + 1)) {
            SoundEvent sound = switch (player.getRandom().nextInt(3)) {
                case 0 -> SoundEvents.ENDERMAN_TELEPORT;
                case 1 -> SoundEvents.CREEPER_PRIMED;
                default -> SoundEvents.CREAKING_AMBIENT;
            };
            player.playSound(sound,
                    0.7f + player.getRandom().nextFloat() * 0.6f,
                    0.8f + player.getRandom().nextFloat() * 0.6f);
        }
    }
}