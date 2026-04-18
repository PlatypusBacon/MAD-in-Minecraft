package bunger.group.client;

import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.client.tyler.god.GodEntityRenderer;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityRenderer;
import bunger.group.tyler.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntities.SQUIRREL, SquirrelEntityRenderer::new);
		EntityRenderers.register(ModEntities.SQUIRREL_BEAR, SquirrelBearEntityRenderer::new);
		EntityRenderers.register(ModEntities.GOD, GodEntityRenderer::new);

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}