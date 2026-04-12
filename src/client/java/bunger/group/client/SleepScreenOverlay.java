package bunger.group.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class SleepScreenOverlay {

    private static final ResourceLocation SLEEP_IMAGE = new ResourceLocation(
            "mutually-assured-destruction", "textures/gui/slep.png"
    );

    public static void register() {
        HudRenderCallback.EVENT.register((poseStack, tickDelta) -> {
            Minecraft client = Minecraft.getInstance();

            // only render while the player is sleeping
            if (client.player == null || !client.player.isSleeping()) return;

            int screenWidth  = client.getWindow().getGuiScaledWidth();
            int screenHeight = client.getWindow().getGuiScaledHeight();

            RenderSystem.setShaderTexture(0, SLEEP_IMAGE);
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            // stretch image to fill the screen
            GuiComponent.blit(poseStack, 0, 0, 0, 0,
                    screenWidth, screenHeight,
                    screenWidth, screenHeight);

            RenderSystem.disableBlend();
        });
    }
}