package bunger.group.client.tyler3.gui;

import bunger.group.client.mixin.ImageButtonAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

@Environment(EnvType.CLIENT)
public final class GuiUtils {
    private GuiUtils() {}

    public static boolean isRecipeButton(Screen screen, AbstractWidget button) {
        if (screen instanceof AbstractContainerScreen && button instanceof ImageButton imageButton) {
            return ((ImageButtonAccessor) imageButton).getSprites()
                    .equals(RecipeBookComponent.RECIPE_BUTTON_SPRITES);
        }
        return false;
    }
}