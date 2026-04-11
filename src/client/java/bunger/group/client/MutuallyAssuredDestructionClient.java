package bunger.group.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.alex.Bunger1;
import bunger.group.client.bryan.Bunger2;
import bunger.group.client.csmit863.Bunger3;
import bunger.group.client.ethan.Bunger4;
import bunger.group.client.ethan.ModEntityModelLayers;
import bunger.group.client.ethan.ProphetEntityRenderer;
import bunger.group.client.tyler.Bunger5;
import bunger.group.ethan.ModEntityTypes;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntityTypes.PROPHET, ProphetEntityRenderer::new);
	}
}