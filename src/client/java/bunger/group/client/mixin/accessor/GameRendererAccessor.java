package bunger.group.client.mixin.accessor;

import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Invoker("setPostEffect")
    void invokeSetPostEffect(Identifier id);
}