package bunger.group.client.tyler3.gui;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class RecipeUnlockedToast implements Toast {

    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/recipe");
    private static final long DISPLAY_TIME = 5000L;
    private static final Component TITLE_TEXT    = Component.literal("Recipe Unlocked!");
    private static final Component SUBTITLE_TEXT = Component.translatable("recipe.toast.description");

    private String recipeId;
    private final Component recipeName;
    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;
    private boolean changed = true;
    private long lastChanged;

    public RecipeUnlockedToast(String recipeId) {
        this.recipeId = recipeId;
        String name = recipeId.replace("_", " ");
        this.recipeName = Component.literal(
                Character.toUpperCase(name.charAt(0)) + name.substring(1)
        );
    }

    @Override
    public Toast.Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        if (changed) {
            lastChanged = fullyVisibleForMs;
            changed = false;
        }
        wantedVisibility = fullyVisibleForMs - lastChanged >= DISPLAY_TIME * manager.getNotificationDisplayTimeMultiplier()
                ? Toast.Visibility.HIDE
                : Toast.Visibility.SHOW;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, Font font, long fullyVisibleForMs) {
        // Same background sprite as vanilla recipe toast
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());

        // Your recipe page texture scaled down as the icon
        Identifier recipeTexture = Identifier.fromNamespaceAndPath(
                MutuallyAssuredDestruction.MOD_ID,
                "textures/gui/tome/recipes/" + recipeId + ".png"
        );
        graphics.pose().pushMatrix();
        graphics.pose().scale(0.6F, 0.6F);
        graphics.blit(RenderPipelines.GUI_TEXTURED, recipeTexture,
                3, 3, 0f, 0f, 26, 20, PaintOverlay.CANVAS_W, PaintOverlay.CANVAS_H);
        graphics.pose().popMatrix();

        graphics.text(font, TITLE_TEXT,    30, 7,  -11534256, false);
        //graphics.text(font, recipeName,    30, 18, -16777216, false);
    }
    public static void show(ToastManager toastManager, String recipeId) {
        toastManager.addToast(new RecipeUnlockedToast(recipeId));
    }
}