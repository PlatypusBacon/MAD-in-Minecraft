package bunger.group.client;

import bunger.group.client.tyler.ModEntityModelLayers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntities.SQUIRREL, SquirrelEntityRenderer::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}