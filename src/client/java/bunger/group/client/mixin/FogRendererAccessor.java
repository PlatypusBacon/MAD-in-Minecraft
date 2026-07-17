package bunger.group.client.mixin;

import net.minecraft.client.renderer.fog.FogRenderer;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.fog.environment.FogEnvironment;

@Mixin(FogRenderer.class)
public interface FogRendererAccessor {
    @Accessor("FOG_ENVIRONMENTS")
    static List<FogEnvironment> getFogEnvironments() {
        throw new AssertionError();
    }
    }
