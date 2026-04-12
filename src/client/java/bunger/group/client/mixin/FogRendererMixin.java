package bunger.group.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bunger.group.client.ethan.RedDarknessFogEnvironment;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addRedDarkness(CallbackInfo ci) {
        System.out.println("USERTEST: FogRendererMixin injecting RedDarknessFogEnvironment");
        FogRendererAccessor.getFogEnvironments().add(0,new RedDarknessFogEnvironment());
    }

}