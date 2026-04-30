package bunger.group.client;

import bunger.group.client.tyler3.PlatformEntityRenderer;
import bunger.group.client.tyler3.goop.StickyGooModel;
import bunger.group.client.tyler3.goop.StickyGooRenderer;
import bunger.group.tyler3.entity.PlatformEntity;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		StickyGooModel.register();
		EntityRenderers.register(bunger.group.tyler3.entity.ModEntities.PLATFORM, PlatformEntityRenderer::new);
		   // must be before world loads
		StickyGooRenderer.register();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}