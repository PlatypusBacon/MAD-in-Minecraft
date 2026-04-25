package bunger.group.client;

import bunger.group.client.tyler.god.GodEntityRenderer;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityRenderer;
import bunger.group.client.tyler.squirrel_wife.SquirrelWifeEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		bunger.group.client.tyler.ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL, SquirrelEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_BEAR, SquirrelBearEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.GOD, GodEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_WIFE, SquirrelWifeEntityRenderer::new);
		bunger.group.client.tyler.SleepScreenOverlay.register();
		bunger.group.client.tyler.bear_boxers.BearBoxersRenderer.register();
		bunger.group.client.tyler.net.PunchSidePacketClient.registerClient();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}