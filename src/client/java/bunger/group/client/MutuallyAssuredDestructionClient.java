package bunger.group.client;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.Mana;
import bunger.group.alex.ManaPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	// Local vars to sync into
	public static int clientMana = 0;
	public static int clientMaxMana = 0;
	private static float displayedMana = 0;
	private static long fullManaTime = -1;
	private static final long HIDE_AFTER_MS = 5000;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(ManaPacket.TYPE, (payload, context) -> {
			clientMana = payload.current();
			clientMaxMana = payload.max();
		});

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_bar"),
				MutuallyAssuredDestructionClient::render
		);
	}

	private static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
		Minecraft client = Minecraft.getInstance();
		if (client.player == null || client.options.hideGui) return;

		int current = clientMana;
		int max = clientMaxMana;
		if (max <= 0) return;

		// Track when mana becomes full
		if (current >= max) {
			if (fullManaTime == -1) fullManaTime = Util.getMillis();
		} else {
			fullManaTime = -1;
		}

		// Hide if full for 5 seconds
		if (fullManaTime != -1 && Util.getMillis() - fullManaTime > HIDE_AFTER_MS) return;

		// Smooth fill animation
		displayedMana += (current - displayedMana) * 0.15f;

		// Position: bottom left corner
		int barWidth = 14;
		int barHeight = 50;
		int x = 8;
		int y = graphics.guiHeight() - barHeight - 8;

		float pct = displayedMana / max;
		int filledHeight = (int)(barHeight * pct);

		// Fade out effect when about to hide
		float alpha = 1.0f;
		if (fullManaTime != -1) {
			long elapsed = Util.getMillis() - fullManaTime;
			if (elapsed > HIDE_AFTER_MS - 1000) {
				alpha = 1.0f - ((elapsed - (HIDE_AFTER_MS - 1000)) / 1000.0f);
				alpha = Math.max(0, Math.min(1, alpha));
			}
		}

		int a = (int)(alpha * 255) << 24;

		// Max mana text - top right of bar
		String maxLabel = String.valueOf(max);
		int maxTextX = x + barWidth + 4;
		graphics.text(client.font, maxLabel, maxTextX, y, a | 0x88CCFF, true);

		// Current mana text - bottom right of bar
		String currentLabel = String.valueOf(current);
		int currentTextX = x + barWidth + 4;
		graphics.text(client.font, currentLabel, currentTextX, y + barHeight - client.font.lineHeight, a | 0x88CCFF, true);

		// Outer glow border
		graphics.fill(x - 2, y - 2, x + barWidth + 2, y + barHeight + 2, a | 0x221133);
		// Inner border
		graphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, a | 0x4B0082);
		// Empty track
		graphics.fill(x, y, x + barWidth, y + barHeight, a | 0x0D0019);

		// Filled portion - gradient from deep purple at bottom to bright cyan at top
		int fillY = y + barHeight - filledHeight;
		graphics.fillGradient(x, fillY, x + barWidth, y + barHeight,
				a | 0x00CCFF,  // bright cyan top
				a | 0x4400AA   // deep purple bottom
		);

		// Shimmer line at the top of the fill
		if (filledHeight > 1) {
			graphics.fill(x, fillY, x + barWidth, fillY + 1, a | 0xAAEEFF);
		}
	}
}