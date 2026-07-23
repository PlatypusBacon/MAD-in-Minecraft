package bunger.group.client.mixin;

import bunger.group.client.MutuallyAssuredDestructionClient;
import bunger.group.csmit863.CustomSounds;
import bunger.group.csmit863.biome.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MadnessClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!player.level().dimension().equals(ModBiomes.MAD_REALM)) return;

        int madness = MutuallyAssuredDestructionClient.clientMadness;
        int maxMadness = MutuallyAssuredDestructionClient.clientMaxMadness;
        if (maxMadness <= 0) return;

        float madnessFactor = (float) madness / maxMadness;
        float chance = 0.0005f * (0.3f + madnessFactor);

        if (player.getRandom().nextFloat() < chance) {
            player.playSound(CustomSounds.OVERSEER_HELLO, 0.8f, 1.0f);
        }

        // Cave ambience scales in frequency with madness
        float caveChance = 0.001f * (0.3f + madnessFactor);
        if (player.getRandom().nextFloat() < caveChance) {
            player.playSound(SoundEvents.ZOMBIE_AMBIENT,
                    0.6f + player.getRandom().nextFloat() * 0.4f,
                    0.8f + player.getRandom().nextFloat() * 0.4f);
        }
    }
}