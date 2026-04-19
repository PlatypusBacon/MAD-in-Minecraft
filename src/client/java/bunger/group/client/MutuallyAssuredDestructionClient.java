package bunger.group.client;

import bunger.group.alex.item.ModItems;
import bunger.group.client.csmit863.entity.ModEntityModelLayers;
import bunger.group.client.csmit863.entity.ShroomjakEntityRenderer;
import bunger.group.csmit863.entity.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntityTypes.SHROOMJAK, ShroomjakEntityRenderer::new);
	}
}