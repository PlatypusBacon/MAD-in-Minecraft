package bunger.group.client;

import bunger.group.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModModelLayers.register();
		SleepScreenOverlay.register();
		EntityRendererRegistry.register(ModEntities.SQUIRREL, SquirrelRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(
				GodRenderer.GOD_LAYER,
				GodRenderer.GodModel::createBodyLayer
		);
		EntityRendererRegistry.register(
				ModEntities.GOD,
				GodRenderer::new
		);

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}