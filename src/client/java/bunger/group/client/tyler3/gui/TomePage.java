package bunger.group.client.tyler3.gui;

import net.minecraft.resources.Identifier;

/**
 * A page in the tome. Either a user-drawn page or a locked/unlocked recipe page.
 */
public sealed interface TomePage permits TomePage.Drawn, TomePage.Recipe {

    /** A user-drawn page — mutable pixel buffer. */
    record Drawn(int[] pixels) implements TomePage {}

    /**
     * A recipe/reference page backed by a texture.
     * The texture should be exactly CANVAS_W × CANVAS_H pixels.
     */
    record Recipe(String recipeId, Identifier texture) implements TomePage {}
}