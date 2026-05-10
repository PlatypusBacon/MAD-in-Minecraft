package bunger.group.tyler3.rego;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Central registry mapping item pickups → recipe page IDs,
 * and recipe page IDs → TomePage.Recipe instances.
 *
 * Add new recipes here. The texture should live at:
 *   assets/<modid>/textures/gui/tome/recipes/<recipeId>.png
 * and must be exactly PaintOverlay.CANVAS_W × PaintOverlay.CANVAS_H pixels (148×60).
 */
public final class RecipePageRegistry {

    // Maps Item → recipe ID (what page to unlock when this item is picked up)
    private static final Map<Item, String> ITEM_TO_RECIPE = new LinkedHashMap<>();
    private static final Map<TagKey<Item>, String> TAG_TO_RECIPE = new LinkedHashMap<>();
    // Ordered list of all known recipe IDs (determines page order in the tome)
    private static final List<String> RECIPE_ORDER = new ArrayList<>();
    private static final Set<String> AUTO_UNLOCKED = new LinkedHashSet<>();

    static {
        // ----------------------------------------------------------------
        // Register your recipes here:
        //   register(Items.COPPER_INGOT, "copper_sword");
        //   register(Items.IRON_INGOT,   "iron_pickaxe");
        // ----------------------------------------------------------------
        // register(Items.COPPER_INGOT, "copper_sword");
    }

    private RecipePageRegistry() {}

    /**
     * Register a recipe page.
     * @param triggerItem  the item whose pickup unlocks this page
     * @param recipeId     stable string ID (used for save/load and texture path)
     */
    public static void register(Item triggerItem, String recipeId) {
        ITEM_TO_RECIPE.put(triggerItem, recipeId);
        if (!RECIPE_ORDER.contains(recipeId)) {
            RECIPE_ORDER.add(recipeId);
        }
    }

    public static void register(String recipeId) {
        if (!RECIPE_ORDER.contains(recipeId)) {
            RECIPE_ORDER.add(recipeId);
        }
        AUTO_UNLOCKED.add(recipeId);
    }
    public static void register(TagKey<Item> triggerTag, String recipeId) {
        TAG_TO_RECIPE.put(triggerTag, recipeId);

        if (!RECIPE_ORDER.contains(recipeId)) {
            RECIPE_ORDER.add(recipeId);
        }
    }
    public static Set<String> autoUnlockedRecipes() {
        return Collections.unmodifiableSet(AUTO_UNLOCKED);
    }

    /** Returns the recipe ID to unlock when this item is picked up, or empty. */
    public static Optional<String> getRecipeIdForItem(Item item) {
        return Optional.ofNullable(ITEM_TO_RECIPE.get(item));
    }

    /** Ordered list of all known recipe IDs. */
    public static List<String> allRecipeIds() {
        return Collections.unmodifiableList(RECIPE_ORDER);
    }
    public static Optional<String> getRecipeIdForItem(ItemStack stack) {

        Item item = stack.getItem();

        // direct item match
        String direct = ITEM_TO_RECIPE.get(item);
        if (direct != null) {
            return Optional.of(direct);
        }

        // tag match
        for (Map.Entry<TagKey<Item>, String> entry : TAG_TO_RECIPE.entrySet()) {
            if (stack.is(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }

}