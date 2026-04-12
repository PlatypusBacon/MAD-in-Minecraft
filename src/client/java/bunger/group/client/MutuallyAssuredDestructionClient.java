package bunger.group.client;

import bunger.group.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModModelLayers.register();
		EntityRendererRegistry.register(ModEntities.SQUIRREL, SquirrelRenderer::new);
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}