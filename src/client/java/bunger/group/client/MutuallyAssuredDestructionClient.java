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
import bunger.group.client.tyler3.hot_plate.HotPlateBlockEntityRenderer;
import bunger.group.client.tyler3.tanning_rack.TanningRackBlockEntityRenderer;
import bunger.group.tyler2.block.ModBlockEntities;
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
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.alex.Bunger1;
import bunger.group.client.bryan.Bunger2;
import bunger.group.client.csmit863.Bunger3;
import bunger.group.client.ethan.Bunger4;
import bunger.group.client.ethan.ModEntityModelLayers;
import bunger.group.client.ethan.ProphetEntityRenderer;
import bunger.group.client.ethan.VoremothCrownClientHandler;
import bunger.group.client.ethan.VoremothEntityRenderer;
import bunger.group.client.tyler.Bunger5;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import bunger.group.ethan.ModEntityTypes;
import bunger.group.ethan.VoremothEntity;

//Alex
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.util.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.MenuScreens;

import bunger.group.alex.ManaPacket;
import bunger.group.alex.menu.ModMenuType;
import bunger.group.client.alex.item.ArbalestPull;
import bunger.group.client.alex.rendering.screens.inventory.SpellDeskScreen;


public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	public static KeyMapping RELOAD_KEY;

	// One overlay instance per open screen — cleaned up on screen close
	private static final Map<Screen, PaintOverlay> OVERLAYS = new HashMap<>();
	private static SlingCharge slingSound = null;

	public static int clientMana = 0;
	public static int clientMaxMana = 0;
	private static float displayedMana = 0;
	private static long fullManaTime = -1;
	private static final long HIDE_AFTER_MS = 5000;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

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
		BlockEntityRenderers.register(
				ModBlockEntities.HOT_PLATE_BE,
				HotPlateBlockEntityRenderer::new
		);
		BlockEntityRenderers.register(
				ModBlockEntities.TANNING_RACK_BE,
				TanningRackBlockEntityRenderer::new
		);
		RecipePageRegistry.register("sling");
		RecipePageRegistry.register("flint_knife");
		RecipePageRegistry.register("spell_quest");
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			PaintOverlay.loadForCurrentWorld();
		});
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
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntityTypes.PROPHET, ProphetEntityRenderer::new);
		EntityRenderers.register(ModEntityTypes.VOREMOTH, VoremothEntityRenderer::new);
		VoremothCrownClientHandler.register();


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.level == null) return;

			for (Entity entity : client.level.entitiesForRendering()) {
				if (!(entity instanceof VoremothEntity voremoth)) continue;

				int targetId = voremoth.getLaserTargetId();
				if (targetId == -1) continue;

				Entity target = client.level.getEntity(targetId);
				if (target == null) continue;

				Vec3 start = new Vec3(voremoth.getEyePosition().x, voremoth.getEyePosition().y - 10, voremoth.getEyePosition().z);
				Vec3 end = new Vec3(target.getEyePosition().x, target.getEyePosition().y - 2, target.getEyePosition().z);
				Vec3 direction = end.subtract(start);
				double length = direction.length();
				Vec3 step = direction.normalize().scale(0.5);

				int numParticles = (int)(length / 0.5);
				for (int i = 0; i < numParticles; i++) {
					Vec3 pos = start.add(step.scale(i));
					client.level.addParticle(ParticleTypes.GLOW, pos.x, pos.y, pos.z, 0, 0, 0);

				}
			}
		});


	HudElementRegistry.attachElementBefore(
		VanillaHudElements.MISC_OVERLAYS,
		Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "crown_tint"),
		(graphics, tickCounter) -> {
			Minecraft client = Minecraft.getInstance();
			if (client.player == null) return;

			ItemStack helmet = client.player.getItemBySlot(EquipmentSlot.HEAD);
			if (!(helmet.getItem() == MutuallyAssuredDestruction.VOREMOTH_CROWN)) return;

			int width = client.getWindow().getGuiScaledWidth();
			int height = client.getWindow().getGuiScaledHeight();
			graphics.fill(0, 0, width, height, 0x33FF0000);
		}
	);
		bunger.group.client.alex.entity.model.ModEntityModelLayers.registerModelLayers();
		bunger.group.client.alex.entity.model.ModEntityModelLayers.registerRenderers();

		MenuScreens.register(ModMenuType.SPELL_DESK, SpellDeskScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(ManaPacket.TYPE, (payload, context) -> {
			clientMana = payload.current();
			clientMaxMana = payload.max();
		});

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_bar"),
				MutuallyAssuredDestructionClient::renderMana
		);

		RangeSelectItemModelProperties.ID_MAPPER.put(
				Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "arbalest_pull"),
				ArbalestPull.MAP_CODEC
		);
	}

	private static void renderMana(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui) return;

		int current = clientMana;
		int max = clientMaxMana;
		if (max <= 0) return;

		if (current >= max) {
			if (fullManaTime == -1) fullManaTime = Util.getMillis();
		} else {
			fullManaTime = -1;
		}

		if (fullManaTime != -1 && Util.getMillis() - fullManaTime > HIDE_AFTER_MS) return;

		displayedMana += (current - displayedMana) * 0.15f;

		int barWidth = 14;
		int barHeight = 50;
		int x = 8;
		int y = graphics.guiHeight() - barHeight - 8;

		float pct = displayedMana / max;
		int filledHeight = (int)(barHeight * pct);

		float alpha = 1.0f;
		if (fullManaTime != -1) {
			long elapsed = Util.getMillis() - fullManaTime;
			if (elapsed > HIDE_AFTER_MS - 1000) {
				alpha = 1.0f - ((elapsed - (HIDE_AFTER_MS - 1000)) / 1000.0f);
				alpha = Math.max(0, Math.min(1, alpha));
			}
		}

		int a = (int)(alpha * 255) << 24;

		String maxLabel = String.valueOf(max);
		graphics.text(client.font, maxLabel, x + barWidth + 4, y, a | 0x88CCFF, true);

		String currentLabel = String.valueOf(current);
		graphics.text(client.font, currentLabel, x + barWidth + 4,
				y + barHeight - client.font.lineHeight, a | 0x88CCFF, true);

		graphics.fill(x - 2, y - 2, x + barWidth + 2, y + barHeight + 2, a | 0x221133);
		graphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, a | 0x4B0082);
		graphics.fill(x, y, x + barWidth, y + barHeight, a | 0x0D0019);

		int fillY = y + barHeight - filledHeight;
		graphics.fillGradient(x, fillY, x + barWidth, y + barHeight,
				a | 0x00CCFF,
				a | 0x4400AA
		);

		if (filledHeight > 1) {
			graphics.fill(x, fillY, x + barWidth, fillY + 1, a | 0xAAEEFF);
		}
	}
}