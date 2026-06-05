package bunger.group.client;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.ManaPacket;
import bunger.group.alex.entity.ModEntityTypes;
import bunger.group.alex.menu.ModMenuType;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.renderer.WraithEntityRenderer;
import bunger.group.client.alex.item.ArbalestPull;
import bunger.group.client.alex.rendering.screens.inventory.SpellDeskScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {

	public static int clientMana = 0;
	public static int clientMaxMana = 0;
	private static float displayedMana = 0;
	private static long fullManaTime = -1;
	private static final long HIDE_AFTER_MS = 5000;

	@Override
	public void onInitializeClient() {
		//Mobs

		ModEntityModelLayers.registerModelLayers();
		ModEntityModelLayers.registerRenderers();

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