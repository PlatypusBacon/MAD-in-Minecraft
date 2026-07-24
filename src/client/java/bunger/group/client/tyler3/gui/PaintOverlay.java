package bunger.group.client.tyler3.gui;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.rego.RecipePageRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.*;

/**
 * Paint overlay UI.
 *
 * Layout (mimics reference UI):
 * ┌─────────────────────────────────────────────────────┐
 * │  [ERASE]  │        CANVAS (book spread)       │ PAL │
 * │           │                                   │ ETT │
 * │           │                                   │  E  │
 * │           │                                   │     │
 * │           │───────────────────────────────────│     │
 * │           │  [brush1][brush2][brush3][brush4] │     │
 * └─────────────────────────────────────────────────────┘
 *
 * - Palette: vertical strip on the RIGHT of the canvas
 * - Brush selector: horizontal strip BELOW the canvas, right-aligned
 * - Erase page button: LEFT side, vertically centred on canvas
 * - Page flip arrows: embedded top-left and top-right of book texture
 *
 * Pages are either user-drawn ({@link TomePage.Drawn}) or recipe reference
 * pages ({@link TomePage.Recipe}) unlocked via item pickup packets from the
 * server. Recipe pages are read-only; drawing controls are hidden on them.
 */
@Environment(EnvType.CLIENT)
public class PaintOverlay {

    // ---------------------------------------------------------------
    // Texture
    // ---------------------------------------------------------------
    private static final Identifier PAINT_TEXTURE = Identifier.fromNamespaceAndPath(
            MutuallyAssuredDestruction.MOD_ID, "textures/gui/tome/paint.png"
    );
    private static final int TEX_SRC_W = 175;
    private static final int TEX_SRC_H = 80;
    public static final int TEX_W = 175;
    public static final int TEX_H = 80;

    // ---------------------------------------------------------------
    // Canvas — inner drawable area of the book texture
    // ---------------------------------------------------------------
    private static final int CANVAS_OFF_X = 6;
    private static final int CANVAS_OFF_Y = 10;
    public static final int CANVAS_W = 148;
    public static final int CANVAS_H = 60;

    // ---------------------------------------------------------------
    // Palette — vertical strip to the RIGHT of the canvas
    // ---------------------------------------------------------------
    private static final int[] PALETTE = {
            0xFF000000, 0xFF444444,
            0xFF888888, 0xFFCCCCCC,
            0xFFFFFFFF, 0xFFFF0000,
            0xFFFF6600, 0xFFFFFF00,
            0xFF00AA00, 0xFF0000FF,
            0xFFAA00AA, 0xFF00AAAA,
            0xFF8B4513, 0xFFFFAAAA,
            0xFF87CEEB, 0xFFFFA500
    };
    private static final int SWATCH     = 9;
    private static final int SWATCH_GAP = 2;
    private static final int PAL_COLS   = 2;
    private static final int PAL_ROWS   = PALETTE.length / PAL_COLS;
    private static final int PAL_OFF_X  = 20;

    // ---------------------------------------------------------------
    // Brush sizes — horizontal strip BELOW canvas, right-aligned
    // ---------------------------------------------------------------
    private static final int[] BRUSH_SIZES = {1, 2, 4, 7};
    private static final int BRUSH_BTN_W = 14;
    private static final int BRUSH_BTN_H = 10;
    private static final int BRUSH_GAP   = 2;
    private static final int BRUSH_OFF_Y = -90;

    // ---------------------------------------------------------------
    // Erase-page button — left of canvas, vertically centred
    // ---------------------------------------------------------------
    private static final int ERASE_W   = 14;
    private static final int ERASE_H   = 14;
    private static final int ERASE_OFF_X = -ERASE_W - 4;

    // ---------------------------------------------------------------
    // Page flip buttons — top corners of the book texture
    // ---------------------------------------------------------------
    private static final int FLIP_BTN_W = 10;
    private static final int FLIP_BTN_H =  8;

    // ---------------------------------------------------------------
    // Global persistent state (shared across all overlay instances)
    // ---------------------------------------------------------------
    private static final List<TomePage>  PAGES          = new ArrayList<>();
    private static final Set<String>     UNLOCKED       = new LinkedHashSet<>();
    private static int                   SAVED_PAGE_INDEX = 0;
    private static boolean               LOADED         = false;

    // ---------------------------------------------------------------
    // Instance state
    // ---------------------------------------------------------------
    private int     pageIndex;
    private int     selectedColor = 0xFF000000;
    private int     brushSize     = 1;
    private boolean drawing       = false;
    private double  lastPaintX    = -1;
    private double  lastPaintY    = -1;
    private boolean visible       = false;

    // Screen-space top-left of the book texture, updated each frame
    private int texX;
    private int texY;

    // ---------------------------------------------------------------
    // Static lifecycle — called from MixinBeGone
    // ---------------------------------------------------------------

    /**
     * Load saved pages for the current world/server.
     * Safe to call multiple times; only loads once per world session.
     * Call from {@code ScreenEvents.AFTER_INIT} before creating overlays.
     */
    public static void loadForCurrentWorld() {
        LOADED = false;
        TomePageStore.setCurrentWorld(); // capture ID while world info is available
        TomePageStore.LoadResult result = TomePageStore.load(CANVAS_W, CANVAS_H, PaintOverlay::buildPage);

        PAGES.clear();
        PAGES.addAll(result.pages());
        UNLOCKED.clear();
        UNLOCKED.addAll(result.unlockedRecipes());
        SAVED_PAGE_INDEX = result.startIndex();

        for (String recipeId : RecipePageRegistry.autoUnlockedRecipes()) {
            unlockRecipe(recipeId);
        }

        LOADED = true;
    }

    /** Persist current state to disk. Call on screen close. */
    public static void saveForCurrentWorld() {
        if (LOADED) TomePageStore.save(PAGES);
    }

    // ---------------------------------------------------------------
    // Recipe unlock (called from packet handler on client thread)
    // ---------------------------------------------------------------

    /**
     * Unlocks a recipe page by ID. Inserts it into the page list in
     * registration order (after other recipe pages, before drawn pages).
     * No-op if already unlocked.
     *
     * @return true if a new page was added
     */
    public static boolean unlockRecipe(String recipeId) {
        MutuallyAssuredDestruction.LOGGER.info("[Tome] unlockRecipe called with: {}, known recipes: {}", recipeId, RecipePageRegistry.allRecipeIds());
        if (UNLOCKED.contains(recipeId)) return false;

        // Validate it's a known recipe
        if (!bunger.group.tyler3.rego.RecipePageRegistry.allRecipeIds().contains(recipeId)) {
            MutuallyAssuredDestruction.LOGGER.warn(
                    "[Tome] Received unlock for unknown recipe: {}", recipeId);
            return false;
        }

        UNLOCKED.add(recipeId);

        int insertAt = 0;
        for (int i = 0; i < PAGES.size(); i++) {
            if (PAGES.get(i) instanceof TomePage.Recipe) {
                insertAt = i + 1;
            }
        }
        PAGES.add(insertAt, buildPage(recipeId));

        // Shift saved page index if we inserted before it
        if (insertAt <= SAVED_PAGE_INDEX) SAVED_PAGE_INDEX++;

        TomePageStore.save(PAGES);
        Minecraft mc = Minecraft.getInstance();
        RecipeUnlockedToast.show(mc.getToastManager(), recipeId);
        return true;
    }
    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------
    /** Builds a TomePage.Recipe for the given ID. */
    public static TomePage.Recipe buildPage(String recipeId) {
        Identifier texture = Identifier.fromNamespaceAndPath(
                MutuallyAssuredDestruction.MOD_ID,
                "textures/gui/tome/recipes/" + recipeId + ".png"
        );
        return new TomePage.Recipe(recipeId, texture);
    }
    public PaintOverlay() {
        if (!LOADED) loadForCurrentWorld();
        this.pageIndex = SAVED_PAGE_INDEX;
    }

    // ---------------------------------------------------------------
    // Visibility
    // ---------------------------------------------------------------
    public boolean isVisible()           { return visible; }
    public void    setVisible(boolean v) { this.visible = v; }
    public void    toggleVisible()       { this.visible = !this.visible; }

    // ---------------------------------------------------------------
    // Position (called every frame by the screen event)
    // ---------------------------------------------------------------
    public void setPosition(int texX, int texY) {
        this.texX = texX;
        this.texY = texY;
    }

    // Canvas top-left in screen space
    private int canvasScreenX() { return texX + CANVAS_OFF_X; }
    private int canvasScreenY() { return texY + CANVAS_OFF_Y; }

    // ---------------------------------------------------------------
    // Page helpers
    // ---------------------------------------------------------------
    private TomePage currentPage() {
        return PAGES.get(pageIndex);
    }

    private boolean isRecipePage() {
        return currentPage() instanceof TomePage.Recipe;
    }

    // ---------------------------------------------------------------
    // Page management
    // ---------------------------------------------------------------
    private void saveCurrentDrawnPage() {
        if (currentPage() instanceof TomePage.Drawn d) {
            PAGES.set(pageIndex, new TomePage.Drawn(Arrays.copyOf(d.pixels(), d.pixels().length)));
        }
    }

    private void goToPage(int index) {
        if (index < 0 || index >= PAGES.size()) return;
        saveCurrentDrawnPage();
        pageIndex        = index;
        SAVED_PAGE_INDEX = index;
    }

    private void erasePage() {
        if (isRecipePage()) return;
        int[] blank = new int[CANVAS_W * CANVAS_H];
        Arrays.fill(blank, 0xFFFFFFFF);
        PAGES.set(pageIndex, new TomePage.Drawn(blank));
        TomePageStore.save(PAGES);
    }

    private void addNewDrawnPage() {
        // Insert a new blank drawn page after the last drawn page (or at end)
        int insertAt = PAGES.size();
        for (int i = PAGES.size() - 1; i >= 0; i--) {
            if (PAGES.get(i) instanceof TomePage.Drawn) {
                insertAt = i + 1;
                break;
            }
        }
        PAGES.add(insertAt, TomePageStore.newBlankDrawn(CANVAS_W, CANVAS_H));
        goToPage(insertAt);
    }

    // ---------------------------------------------------------------
    // Rendering
    // ---------------------------------------------------------------
    public void render(GuiGraphicsExtractor graphics) {
        // Book background texture
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                PAINT_TEXTURE,
                texX, texY,
                0f, 0f,
                TEX_W, TEX_H,
                TEX_SRC_W, TEX_SRC_H
        );

        int cx = canvasScreenX();
        int cy = canvasScreenY();

        switch (currentPage()) {
            case TomePage.Drawn d -> renderDrawnPage(graphics, d, cx, cy);
            case TomePage.Recipe r -> renderRecipePage(graphics, r, cx, cy);
        }

        renderPageButtons(graphics);
        renderPageNumber(graphics);

        // Drawing controls are only shown on drawn pages
        if (!isRecipePage()) {
            renderPalette(graphics);
            renderBrushButtons(graphics);
            renderEraseButton(graphics);
        }
    }

    private void renderDrawnPage(GuiGraphicsExtractor graphics, TomePage.Drawn d, int cx, int cy) {
        int[] pixels = d.pixels();
        for (int py = 0; py < CANVAS_H; py++) {
            for (int px = 0; px < CANVAS_W; px++) {
                int color = pixels[py * CANVAS_W + px];
                if (color == 0xFFFFFFFF) continue;
                graphics.fill(cx + px, cy + py, cx + px + 1, cy + py + 1, color);
            }
        }
    }

    private void renderRecipePage(GuiGraphicsExtractor graphics, TomePage.Recipe r, int cx, int cy) {
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                r.texture(),
                cx, cy,
                0f, 0f,
                CANVAS_W, CANVAS_H,
                CANVAS_W, CANVAS_H
        );
    }

    /** Vertical two-column palette to the right of the canvas. */
    private void renderPalette(GuiGraphicsExtractor graphics) {
        int palX = canvasScreenX() + CANVAS_W + PAL_OFF_X;
        int palY = canvasScreenY() - SWATCH + SWATCH_GAP;

        for (int i = 0; i < PALETTE.length; i++) {
            int col = i % PAL_COLS;
            int row = i / PAL_COLS;
            int sx  = palX + col * (SWATCH + SWATCH_GAP);
            int sy  = palY + row * (SWATCH + SWATCH_GAP);

            boolean selected = PALETTE[i] == selectedColor;
            if (selected) {
                graphics.fill(sx - 1, sy - 1, sx + SWATCH + 1, sy + SWATCH + 1, 0xFFFFFFFF);
                graphics.fill(sx,     sy,     sx + SWATCH,     sy + SWATCH,     0xFF000000);
                graphics.fill(sx + 1, sy + 1, sx + SWATCH - 1, sy + SWATCH - 1, PALETTE[i]);
            } else {
                graphics.fill(sx, sy, sx + SWATCH, sy + SWATCH, PALETTE[i]);
            }
        }
    }

    /** Horizontal brush-size buttons below the canvas, right-aligned with palette. */
    private void renderBrushButtons(GuiGraphicsExtractor graphics) {
        int totalW    = BRUSH_SIZES.length * (BRUSH_BTN_W + BRUSH_GAP) - BRUSH_GAP;
        int palRightX = canvasScreenX() + CANVAS_W + PAL_OFF_X
                + PAL_COLS * (SWATCH + SWATCH_GAP) - SWATCH_GAP;
        int startX = palRightX - totalW;
        int startY = canvasScreenY() + CANVAS_H + BRUSH_OFF_Y;

        for (int i = 0; i < BRUSH_SIZES.length; i++) {
            int bx  = startX + i * (BRUSH_BTN_W + BRUSH_GAP);
            boolean sel = BRUSH_SIZES[i] == brushSize;

            graphics.fill(bx, startY, bx + BRUSH_BTN_W, startY + BRUSH_BTN_H,
                    sel ? 0xFFDDDDCC : 0xFF888877);
            int d   = Math.min(BRUSH_SIZES[i] * 2 - 1, BRUSH_BTN_W - 2);
            int off = (BRUSH_BTN_W - d) / 2;
            graphics.fill(bx + off, startY + (BRUSH_BTN_H - d) / 2,
                    bx + off + d, startY + (BRUSH_BTN_H - d) / 2 + d,
                    0xFF000000);
        }
    }

    /** Small "erase page" button to the left of the canvas. */
    private void renderEraseButton(GuiGraphicsExtractor graphics) {
        int ex = canvasScreenX() + ERASE_OFF_X;
        int ey = canvasScreenY() + (CANVAS_H - ERASE_H) / 2;
        graphics.fill(ex, ey, ex + ERASE_W, ey + ERASE_H, 0xFFAA6644);
        int m = 3;
        graphics.fill(ex + m,               ey + m,               ex + ERASE_W - m, ey + m + 2,           0xFFFFFFFF);
        graphics.fill(ex + m,               ey + ERASE_H - m - 2, ex + ERASE_W - m, ey + ERASE_H - m,     0xFFFFFFFF);
        graphics.fill(ex + m,               ey + m,               ex + m + 2,       ey + ERASE_H - m,     0xFFFFFFFF);
        graphics.fill(ex + ERASE_W - m - 2, ey + m,               ex + ERASE_W - m, ey + ERASE_H - m,     0xFFFFFFFF);
    }

    private void renderPageNumber(GuiGraphicsExtractor graphics) {
        // Show page number and a small "R" indicator for recipe pages
        String pageNum  = "" + (pageIndex + 1);
        String label    = isRecipePage() ? pageNum + "R" : pageNum;
        int textW = label.length() * 6;
        int tx = texX + (TEX_W - textW) / 2;
        int ty = texY + 2;
        graphics.text(Minecraft.getInstance().font, label, tx, ty, 0xFF000000, false);
    }

    /** Left/right page-flip arrows. Right arrow also creates a new page if at the last drawn page. */
    private void renderPageButtons(GuiGraphicsExtractor graphics) {
        // Previous — top-left
        int prevX = texX + 2, prevY = texY + 2;
        graphics.fill(prevX, prevY, prevX + FLIP_BTN_W, prevY + FLIP_BTN_H,
                pageIndex > 0 ? 0xFFAA9977 : 0xFF555544);
        graphics.fill(prevX + 4, prevY + 2,             prevX + 7, prevY + FLIP_BTN_H - 2, 0xFF000000);
        graphics.fill(prevX + 2, prevY + FLIP_BTN_H / 2 - 1, prevX + 5, prevY + FLIP_BTN_H / 2 + 1, 0xFF000000);

        // Next — top-right (always active; creates new page if at end)
        int nextX = texX + TEX_W - FLIP_BTN_W - 2, nextY = texY + 2;
        graphics.fill(nextX, nextY, nextX + FLIP_BTN_W, nextY + FLIP_BTN_H, 0xFFAA9977);
        graphics.fill(nextX + 3, nextY + 2,             nextX + 6, nextY + FLIP_BTN_H - 2, 0xFF000000);
        graphics.fill(nextX + 5, nextY + FLIP_BTN_H / 2 - 1, nextX + 8, nextY + FLIP_BTN_H / 2 + 1, 0xFF000000);
    }

    // ---------------------------------------------------------------
    // Mouse handling
    // ---------------------------------------------------------------
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return false;
        return handleClick(mx, my);
    }

    public boolean mouseDragged(double mx, double my, double deltaX, double deltaY) {
        if (!drawing) return false;
        return handleDrag(mx, my, deltaX, deltaY);
    }

    public boolean mouseReleased(double mx, double my, int button) {
        if (button != 0) return false;
        if (drawing) {
            drawing    = false;
            lastPaintX = -1;
            lastPaintY = -1;
            saveCurrentDrawnPage();
            TomePageStore.save(PAGES);
            return true;
        }
        return false;
    }

    // ---------------------------------------------------------------
    // Internal input
    // ---------------------------------------------------------------
    private boolean handleClick(double mx, double my) {
        // Page flip — top corners
        int prevX = texX + 2, prevY = texY + 2;
        if (inBox(mx, my, prevX, prevY, FLIP_BTN_W, FLIP_BTN_H)) {
            goToPage(pageIndex - 1);
            return true;
        }
        int nextX = texX + TEX_W - FLIP_BTN_W - 2, nextY = texY + 2;
        if (inBox(mx, my, nextX, nextY, FLIP_BTN_W, FLIP_BTN_H)) {
            if (pageIndex < PAGES.size() - 1) {
                goToPage(pageIndex + 1);
            } else {
                addNewDrawnPage();
            }
            return true;
        }

        // Controls below are only active on drawn pages
        if (!isRecipePage()) {
            // Erase button
            int ex = canvasScreenX() + ERASE_OFF_X;
            int ey = canvasScreenY() + (CANVAS_H - ERASE_H) / 2;
            if (inBox(mx, my, ex, ey, ERASE_W, ERASE_H)) {
                erasePage();
                return true;
            }

            // Palette
            int palX = canvasScreenX() + CANVAS_W + PAL_OFF_X;
            int palY = canvasScreenY() - SWATCH + SWATCH_GAP;
            for (int i = 0; i < PALETTE.length; i++) {
                int col = i % PAL_COLS, row = i / PAL_COLS;
                int sx  = palX + col * (SWATCH + SWATCH_GAP);
                int sy  = palY + row * (SWATCH + SWATCH_GAP);
                if (inBox(mx, my, sx, sy, SWATCH, SWATCH)) {
                    selectedColor = PALETTE[i];
                    return true;
                }
            }

            // Brush size buttons
            int palRightX   = canvasScreenX() + CANVAS_W + PAL_OFF_X
                    + PAL_COLS * (SWATCH + SWATCH_GAP) - SWATCH_GAP;
            int totalW      = BRUSH_SIZES.length * (BRUSH_BTN_W + BRUSH_GAP) - BRUSH_GAP;
            int brushStartX = palRightX - totalW;
            int brushStartY = canvasScreenY() + CANVAS_H + BRUSH_OFF_Y;
            for (int i = 0; i < BRUSH_SIZES.length; i++) {
                int bx = brushStartX + i * (BRUSH_BTN_W + BRUSH_GAP);
                if (inBox(mx, my, bx, brushStartY, BRUSH_BTN_W, BRUSH_BTN_H)) {
                    brushSize = BRUSH_SIZES[i];
                    return true;
                }
            }

            // Canvas
            if (isOnCanvas(mx, my)) {
                drawing    = true;
                lastPaintX = mx;
                lastPaintY = my;
                paintAt(mx, my);
                return true;
            }
        }

        return false;
    }

    private boolean handleDrag(double mx, double my, double deltaX, double deltaY) {
        if (!drawing || isRecipePage()) return false;
        if (lastPaintX >= 0) {
            double dist  = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            int    steps = Math.max(1, (int) Math.ceil(dist));
            for (int s = 0; s <= steps; s++) {
                double t  = (double) s / steps;
                double ix = lastPaintX + (mx - lastPaintX) * t;
                double iy = lastPaintY + (my - lastPaintY) * t;
                paintAt(ix, iy);
            }
        } else {
            paintAt(mx, my);
        }
        lastPaintX = mx;
        lastPaintY = my;
        return true;
    }

    public boolean isOnCanvas(double mx, double my) {
        return inBox(mx, my, canvasScreenX(), canvasScreenY(), CANVAS_W, CANVAS_H);
    }

    public boolean isOnOverlay(double mx, double my) {
        return inBox(mx, my, texX, texY, TEX_W, TEX_H);
    }

    private void paintAt(double mx, double my) {
        if (!(currentPage() instanceof TomePage.Drawn d)) return;
        int px   = (int)(mx - canvasScreenX());
        int py   = (int)(my - canvasScreenY());
        int half = brushSize / 2;
        int[] pixels = d.pixels();
        for (int dy = -half; dy <= half; dy++) {
            for (int dx = -half; dx <= half; dx++) {
                int nx = px + dx, ny = py + dy;
                if (nx >= 0 && nx < CANVAS_W && ny >= 0 && ny < CANVAS_H) {
                    pixels[ny * CANVAS_W + nx] = selectedColor;
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // Utility
    // ---------------------------------------------------------------
    private static boolean inBox(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }
}