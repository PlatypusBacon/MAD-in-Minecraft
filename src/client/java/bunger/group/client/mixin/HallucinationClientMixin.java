package bunger.group.client.mixin;

import bunger.group.csmit863.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class HallucinationClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null) return;

        var effect = player.getEffect(ModItems.HALLUCINATION_EFFECT);
        if (effect == null) return;

        int amplifier = effect.getAmplifier();
        if (amplifier < 2) return;

        if (player.getRandom().nextFloat() < 0.004f * amplifier) {
            SoundEvent sound;
            int r = player.getRandom().nextInt(3);
            sound = switch (r) {
                case 0 -> SoundEvents.ENDERMAN_TELEPORT;
                case 1 -> SoundEvents.GHAST_WARN;
                default -> SoundEvents.CREEPER_PRIMED;
            };
            float volume = 0.7f + player.getRandom().nextFloat() * 0.6f;
            float pitch = 0.8f + player.getRandom().nextFloat() * 0.6f;
            player.playSound(sound, volume, pitch);
        }
    }
}