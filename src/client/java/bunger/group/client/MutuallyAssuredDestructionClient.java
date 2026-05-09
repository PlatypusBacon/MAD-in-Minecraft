package bunger.group.client;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.TomeClientNetworking;
import bunger.group.client.tyler3.MixinBeGone;
import bunger.group.client.tyler3.SlingCharge;
import bunger.group.client.tyler3.deer.DeerEntityRenderer;
import bunger.group.client.tyler3.gui.PaintOverlay;
import bunger.group.client.tyler.god.GodEntityRenderer;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityRenderer;
import bunger.group.client.tyler.squirrel_wife.SquirrelWifeEntityRenderer;
import bunger.group.client.tyler3.IsFiring;
import bunger.group.client.tyler3.dude.DudeRenderer;
import bunger.group.tyler2.item.SlingItem;
import bunger.group.tyler3.rego.RecipePageRegistry;
import bunger.group.client.tyler3.shopping_cart.ShoppingCartRenderer;
import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler.item.ModItems;
import bunger.group.tyler3.network.ReloadPacket;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	public static KeyMapping RELOAD_KEY;

	// One overlay instance per open screen — cleaned up on screen close
	private static final Map<Screen, PaintOverlay> OVERLAYS = new HashMap<>();
	private static SlingCharge slingSound = null;
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

		EntityRendererRegistry.register(
				bunger.group.tyler3.entity.ModEntities.ROCK_PROJECTILE,
				ThrownItemRenderer::new
		);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			if (client.player.isUsingItem()
					&& client.player.getUseItem().getItem() instanceof SlingItem) {
				// Only start if not already playing
				if (!client.getSoundManager().isActive(slingSound) || slingSound == null) {
					slingSound = new SlingCharge(client.player);
					client.getSoundManager().play(slingSound);
				}
			}
		});
		// 3. Register your recipes (also in onInitialize, or a dedicated init method):
		TomeClientNetworking.register();
		bunger.group.client.tyler.ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL, SquirrelEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_BEAR, SquirrelBearEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.GOD, GodEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler.entity.ModEntities.SQUIRREL_WIFE, SquirrelWifeEntityRenderer::new);
		EntityRenderers.register(bunger.group.tyler3.entity.ModEntities.DEER, DeerEntityRenderer::new);

		bunger.group.client.tyler.SleepScreenOverlay.register();
		EntityRenderers.register(bunger.group.tyler3.entity.ModEntities.DUDE, DudeRenderer::new);
		EntityRenderers.register(ModEntities.SHOPPING_CART, ShoppingCartRenderer::new);
		bunger.group.client.tyler.bear_boxers.BearBoxersRenderer.register();
		bunger.group.client.tyler.net.PunchSidePacketClient.registerClient();
		bunger.group.client.tyler3.TheNotoriousBeej.register();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (RELOAD_KEY.consumeClick()) {
				ClientPlayNetworking.send(new ReloadPacket());
			}
		});


		MixinBeGone.register();
	}
}