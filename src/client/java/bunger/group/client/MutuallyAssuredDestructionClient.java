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
import bunger.group.tyler3.network.ReloadPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.CustomData;
import bunger.group.client.tyler.AliciaKeys;


public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	public static KeyMapping RELOAD_KEY;
	@Override
	public void onInitializeClient() {
		RELOAD_KEY = KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						"key.mutually-assured-destruction.reload",
						InputConstants.Type.KEYSYM,
						InputConstants.KEY_R,
						KeyMapping.Category.GAMEPLAY
				)
		);
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
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (RELOAD_KEY.consumeClick()) {
				ClientPlayNetworking.send(new ReloadPacket());
			}
		});
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}