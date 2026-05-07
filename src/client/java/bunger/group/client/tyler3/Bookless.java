package bunger.group.client.tyler3;

import bunger.group.client.mixin.ImageButtonAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

public final class Bookless {
    private Bookless() {} // utility class, no instantiation

    public static boolean isRecipeButton(Screen screen, AbstractWidget button) {
        if (screen instanceof AbstractContainerScreen && button instanceof ImageButton imageButton) {
            return ((ImageButtonAccessor) imageButton).getSprites()
                    .equals(RecipeBookComponent.RECIPE_BUTTON_SPRITES);
        }
        return false;
    }
}