package bunger.group.client;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.god.GodEntityRenderer;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityRenderer;
import bunger.group.client.tyler.squirrel_wife.SquirrelWifeEntityRenderer;
import bunger.group.client.tyler3.IsFiring;
import bunger.group.client.tyler3.dude.DudeRenderer;
import bunger.group.client.tyler3.shopping_cart.ShoppingCartRenderer;
import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler3.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.CustomData;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConditionalItemModelProperties.ID_MAPPER.put(
				Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "firing"),
				IsFiring.MAP_CODEC
		);
		bunger.group.client.tyler.ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL, SquirrelEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_BEAR, SquirrelBearEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.GOD, GodEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_WIFE, SquirrelWifeEntityRenderer::new);
		bunger.group.client.tyler.SleepScreenOverlay.register();
		EntityRenderers.register(bunger.group.tyler3.entity.ModEntities.DUDE, DudeRenderer::new);
		EntityRenderers.register(ModEntities.SHOPPING_CART, ShoppingCartRenderer::new);
		   // must be before world loads
		bunger.group.client.tyler.bear_boxers.BearBoxersRenderer.register();
		bunger.group.client.tyler.net.PunchSidePacketClient.registerClient();

		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}