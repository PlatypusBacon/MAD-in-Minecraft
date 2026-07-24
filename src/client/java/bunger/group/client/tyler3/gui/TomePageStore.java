package bunger.group.client.tyler3.gui;

import bunger.group.MutuallyAssuredDestruction;
import com.google.gson.*;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Handles saving and loading of all tome page state.
 *
 * Save format (JSON):
 * {
 *   "unlockedRecipes": ["copper_sword", "iron_pickaxe"],
 *   "pages": [
 *     [16777215, 0, ...],   // page 0 as int[] (CANVAS_W * CANVAS_H ints)
 *     [...]
 *   ]
 * }
 *
 * Recipe pages are reconstructed from "unlockedRecipes" on load — they are
 * inserted at the front of the page list in registration order.
 * Drawn pages follow after.
 */
public final class TomePageStore {

    private static final Gson GSON = new GsonBuilder().create();

    private TomePageStore() {}

    // ---------------------------------------------------------------
    // World ID
    // ---------------------------------------------------------------


    private static String cachedWorldId = null;

    public static void setCurrentWorld() {
        cachedWorldId = currentWorldId();
    }

    private static Path getSavePath() {
        String id = cachedWorldId != null ? cachedWorldId : currentWorldId();
        return Minecraft.getInstance().gameDirectory.toPath()
                .resolve("tome_pages")
                .resolve(id + ".json");
    }
    /**
     * Returns a filesystem-safe identifier for the current world/server.
     * Used as the save file name.
     */
    public static String currentWorldId() {
        var client = Minecraft.getInstance();
        if (client.isLocalServer() && client.getSingleplayerServer() != null) {
            return sanitize(client.getSingleplayerServer().getWorldData().getLevelName());
        }
        if (client.getCurrentServer() != null) {
            return sanitize(client.getCurrentServer().ip);
        }
        return "unknown";
    }

    private static String sanitize(String raw) {
        return raw.replaceAll("[^a-zA-Z0-9._\\-]", "_");
    }

    // ---------------------------------------------------------------
    // Save
    // ---------------------------------------------------------------

    /**
     * Serialise the current page list to disk.
     * Recipe pages are stored as their IDs; drawn pages as int[] pixel arrays.
     */
    public static void save(List<TomePage> pages) {
        try {
            Path path = getSavePath();
            Files.createDirectories(path.getParent());

            JsonObject root = new JsonObject();

            // Unlocked recipe IDs (ordered)
            JsonArray recipes = new JsonArray();
            pages.stream()
                    .filter(p -> p instanceof TomePage.Recipe)
                    .map(p -> ((TomePage.Recipe) p).recipeId())
                    .forEach(recipes::add);
            root.add("unlockedRecipes", recipes);

            // Drawn page pixel arrays
            JsonArray drawn = new JsonArray();
            pages.stream()
                    .filter(p -> p instanceof TomePage.Drawn)
                    .map(p -> ((TomePage.Drawn) p).pixels())
                    .forEach(pixels -> {
                        JsonArray arr = new JsonArray();
                        for (int px : pixels) arr.add(px);
                        drawn.add(arr);
                    });
            root.add("pages", drawn);

            Files.writeString(path, GSON.toJson(root));
        } catch (IOException e) {
            MutuallyAssuredDestruction.LOGGER.error("[Tome] Failed to save pages", e);
        }
    }

    // ---------------------------------------------------------------
    // Load
    // ---------------------------------------------------------------

    /**
     * Loads and returns the page list for the current world.
     * Returns a default list (one blank drawn page, no recipe pages) if no save exists.
     */
    public static LoadResult load(int canvasW, int canvasH, java.util.function.Function<String, TomePage.Recipe> buildPage) {
        Path path = getSavePath();

        List<TomePage> pages = new ArrayList<>();
        Set<String> unlocked = new LinkedHashSet<>();

        if (Files.exists(path)) {
            try {
                String json = Files.readString(path);
                JsonObject root = GSON.fromJson(json, JsonObject.class);

                // Unlocked recipes — rebuild in registration order so page order is stable
                if (root.has("unlockedRecipes")) {
                    for (JsonElement el : root.getAsJsonArray("unlockedRecipes")) {
                        unlocked.add(el.getAsString());
                    }
                }

                // Drawn pages
                if (root.has("pages")) {
                    for (JsonElement pageEl : root.getAsJsonArray("pages")) {
                        JsonArray arr = pageEl.getAsJsonArray();
                        if (arr.size() == canvasW * canvasH) {
                            int[] pixels = new int[arr.size()];
                            for (int i = 0; i < arr.size(); i++) {
                                pixels[i] = arr.get(i).getAsInt();
                            }
                            pages.add(new TomePage.Drawn(pixels));
                        }
                    }
                }
            } catch (IOException | JsonParseException e) {
                MutuallyAssuredDestruction.LOGGER.error("[Tome] Failed to load pages", e);
            }
        }

        // dont prepend claude what were you doing
        List<TomePage> recipePages = new ArrayList<>();
        for (String id : unlocked) {
            recipePages.add(buildPage.apply(id));
        }

        List<TomePage> all = new ArrayList<>();
        all.addAll(recipePages);
        all.addAll(pages);

        // Always have at least one drawn page
        if (all.stream().noneMatch(p -> p instanceof TomePage.Drawn)) {
            all.add(newBlankDrawn(canvasW, canvasH));
        }

        // Start index: first drawn page (after all recipe pages)
        int startIndex = recipePages.size();

        return new LoadResult(all, startIndex, unlocked);
    }

    public static TomePage.Drawn newBlankDrawn(int canvasW, int canvasH) {
        int[] pixels = new int[canvasW * canvasH];
        Arrays.fill(pixels, 0xFFFFFFFF);
        return new TomePage.Drawn(pixels);
    }

    /** Result from a load operation. */
    public record LoadResult(List<TomePage> pages, int startIndex, Set<String> unlockedRecipes) {}
}