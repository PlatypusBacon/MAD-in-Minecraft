package bunger.group.client.tyler;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class SleepScreenOverlay {

    private static final Identifier SLEEP_IMAGE = Identifier.fromNamespaceAndPath(
            "mutually-assured-destruction", "textures/gui/slep.png"
    );

    private static final Identifier HUD_ID = Identifier.fromNamespaceAndPath(
            "mutually-assured-destruction", "sleep_overlay"
    );

    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.SLEEP,
                HUD_ID,
                new HudElement() {
                    @Override
                    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
                        Minecraft client = Minecraft.getInstance();
                        if (client.player == null || !client.player.isSleeping()) return;

                        int w = graphics.guiWidth();
                        int h = graphics.guiHeight();

                        graphics.blit(
                                RenderPipelines.GUI_TEXTURED,
                                SLEEP_IMAGE,
                                0, 0,
                                0.0f, 0.0f,
                                w, h,
                                w, h
                        );
                    }
                }
        );
    }
}