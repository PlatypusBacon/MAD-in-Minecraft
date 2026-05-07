package bunger.group.client.tyler3.gui;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 */
@Environment(EnvType.CLIENT)
public class PaintOverlay {

    // ---------------------------------------------------------------
    // Texture
    // ---------------------------------------------------------------
    private static final Identifier PAINT_TEXTURE = Identifier.fromNamespaceAndPath(
            MutuallyAssuredDestruction.MOD_ID, "textures/gui/sprites/paint.png"
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
    public static final int CANVAS_H = 60;  // leave room for brush strip below

    // ---------------------------------------------------------------
    // Palette — vertical strip to the RIGHT of the canvas
    // Two columns of swatches so it doesn't get too tall
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
    private static final int SWATCH       = 9;   // swatch size in px
    private static final int SWATCH_GAP   = 2;
    private static final int PAL_COLS     = 2;   // two-column palette
    private static final int PAL_ROWS     = PALETTE.length / PAL_COLS;
    // Horizontal gap between canvas right edge and palette
    private static final int PAL_OFF_X    = 20;

    // ---------------------------------------------------------------
    // Brush sizes — horizontal strip BELOW canvas, right-aligned
    // ---------------------------------------------------------------
    private static final int[] BRUSH_SIZES = {1, 2, 4, 7};
    private static final int BRUSH_BTN_W  = 14;
    private static final int BRUSH_BTN_H  = 10;
    private static final int BRUSH_GAP    = 2;
    private static final int BRUSH_OFF_Y  = -90; // gap between canvas bottom and buttons

    // ---------------------------------------------------------------
    // Erase-page button — left of canvas, vertically centred
    // ---------------------------------------------------------------
    private static final int ERASE_W = 14;
    private static final int ERASE_H = 14;
    private static final int ERASE_OFF_X = -ERASE_W - 4; // left of canvas

    // ---------------------------------------------------------------
    // Page flip buttons — top corners of the book texture
    // ---------------------------------------------------------------
    private static final int FLIP_BTN_W = 10;
    private static final int FLIP_BTN_H =  8;

    // ---------------------------------------------------------------
    // Global persistent state
    // ---------------------------------------------------------------
    private static final List<int[]> SAVED_PAGES = new ArrayList<>();
    private static int SAVED_PAGE_INDEX = 0;

    static {
        SAVED_PAGES.add(newBlankCanvas());
    }

    // ---------------------------------------------------------------
    // Instance state
    // ---------------------------------------------------------------
    private int[]   pixels;
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

    public PaintOverlay() {
        this.pageIndex = SAVED_PAGE_INDEX;
        this.pixels    = SAVED_PAGES.get(pageIndex);
    }

    // ---------------------------------------------------------------
    // Visibility
    // ---------------------------------------------------------------
    public boolean isVisible()               { return visible; }
    public void    setVisible(boolean v)     { this.visible = v; }
    public void    toggleVisible()           { this.visible = !this.visible; }

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
    // Page management
    // ---------------------------------------------------------------
    private void saveCurrentPage() {
        SAVED_PAGES.set(pageIndex, Arrays.copyOf(pixels, pixels.length));
    }

    private void goToPage(int index) {
        saveCurrentPage();
        if (index < 0) return;
        while (SAVED_PAGES.size() <= index) SAVED_PAGES.add(newBlankCanvas());
        pageIndex        = index;
        SAVED_PAGE_INDEX = index;
        pixels           = SAVED_PAGES.get(pageIndex);
    }

    private void erasePage() {
        Arrays.fill(pixels, 0xFFFFFFFF);
        saveCurrentPage();
    }

    private static int[] newBlankCanvas() {
        int[] p = new int[CANVAS_W * CANVAS_H];
        Arrays.fill(p, 0xFFFFFFFF);
        return p;
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

        // Canvas pixels
        int cx = canvasScreenX();
        int cy = canvasScreenY();
        for (int py = 0; py < CANVAS_H; py++) {
            for (int px = 0; px < CANVAS_W; px++) {
                int color = pixels[py * CANVAS_W + px];
                if (color == 0xFFFFFFFF) continue; // transparent — show texture
                graphics.fill(cx + px, cy + py, cx + px + 1, cy + py + 1, color);
            }
        }

        renderPalette(graphics);
        renderBrushButtons(graphics);
        renderEraseButton(graphics);
        renderPageButtons(graphics);
        renderPageNumber(graphics);
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
                // White outline then black border
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
        int totalW  = BRUSH_SIZES.length * (BRUSH_BTN_W + BRUSH_GAP) - BRUSH_GAP;
        // Right-align with the right edge of the palette area
        int palRightX = canvasScreenX() + CANVAS_W + PAL_OFF_X
                + PAL_COLS * (SWATCH + SWATCH_GAP) - SWATCH_GAP;
        int startX  = palRightX - totalW;
        int startY  = canvasScreenY() + CANVAS_H + BRUSH_OFF_Y;

        for (int i = 0; i < BRUSH_SIZES.length; i++) {
            int bx  = startX + i * (BRUSH_BTN_W + BRUSH_GAP);
            boolean sel = BRUSH_SIZES[i] == brushSize;

            // Button background
            graphics.fill(bx, startY, bx + BRUSH_BTN_W, startY + BRUSH_BTN_H,
                    sel ? 0xFFDDDDCC : 0xFF888877);
            // Dot preview — scales with brush size, clamped to button interior
            int d   = Math.min(BRUSH_SIZES[i] * 2 - 1, BRUSH_BTN_W - 2);
            int off = (BRUSH_BTN_W - d) / 2;
            graphics.fill(bx + off, startY + (BRUSH_BTN_H - d) / 2,
                    bx + off + d, startY + (BRUSH_BTN_H - d) / 2 + d,
                    0xFF000000);
        }
    }

    /** Small "erase page" button to the left of the canvas, vertically centred. */
    private void renderEraseButton(GuiGraphicsExtractor graphics) {
        int ex = canvasScreenX() + ERASE_OFF_X;
        int ey = canvasScreenY() + (CANVAS_H - ERASE_H) / 2;
        // Background
        graphics.fill(ex, ey, ex + ERASE_W, ey + ERASE_H, 0xFFAA6644);
        // Simple X icon (two crossing bars)
        int m = 3;
        graphics.fill(ex + m,           ey + m,           ex + ERASE_W - m, ey + m + 2,           0xFFFFFFFF);
        graphics.fill(ex + m,           ey + ERASE_H - m - 2, ex + ERASE_W - m, ey + ERASE_H - m, 0xFFFFFFFF);
        graphics.fill(ex + m,           ey + m,           ex + m + 2,       ey + ERASE_H - m,     0xFFFFFFFF);
        graphics.fill(ex + ERASE_W - m - 2, ey + m, ex + ERASE_W - m, ey + ERASE_H - m,          0xFFFFFFFF);
    }

    private void renderPageNumber(GuiGraphicsExtractor graphics) {
        String text = ""+(pageIndex + 1);
        // Centred horizontally across the book texture
        int textW = text.length() * 6; // vanilla font is ~6px per char
        int tx = texX + (TEX_W - textW) / 2;
        int ty = texY + 2;
        graphics.text(Minecraft.getInstance().font, text, tx, ty, 0xFF000000, false);
    }
    /** Left/right page-flip arrows at the top corners of the book texture. */
    private void renderPageButtons(GuiGraphicsExtractor graphics) {
        // Previous — top-left
        int prevX = texX + 2;
        int prevY = texY + 2;
        graphics.fill(prevX, prevY, prevX + FLIP_BTN_W, prevY + FLIP_BTN_H,
                pageIndex > 0 ? 0xFFAA9977 : 0xFF555544);
        // < arrow (simple filled triangle approximation)
        graphics.fill(prevX + 4, prevY + 2, prevX + 7, prevY + FLIP_BTN_H - 2, 0xFF000000);
        graphics.fill(prevX + 2, prevY + FLIP_BTN_H / 2 - 1, prevX + 5, prevY + FLIP_BTN_H / 2 + 1, 0xFF000000);

        // Next — top-right
        int nextX = texX + TEX_W - FLIP_BTN_W - 2;
        int nextY = texY + 2;
        graphics.fill(nextX, nextY, nextX + FLIP_BTN_W, nextY + FLIP_BTN_H, 0xFFAA9977);
        // > arrow
        graphics.fill(nextX + 3, nextY + 2, nextX + 6, nextY + FLIP_BTN_H - 2, 0xFF000000);
        graphics.fill(nextX + 5, nextY + FLIP_BTN_H / 2 - 1, nextX + 8, nextY + FLIP_BTN_H / 2 + 1, 0xFF000000);
    }

    // ---------------------------------------------------------------
    // Mouse handling (called from PaintOverlayScreenHandler)
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
            saveCurrentPage();
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
            goToPage(pageIndex + 1);
            return true;
        }

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
        int palRightX = palX + PAL_COLS * (SWATCH + SWATCH_GAP) - SWATCH_GAP;
        int totalW    = BRUSH_SIZES.length * (BRUSH_BTN_W + BRUSH_GAP) - BRUSH_GAP;
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

        return false;
    }

    private boolean handleDrag(double mx, double my, double deltaX, double deltaY) {
        if (!drawing) return false;
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
        int px   = (int)(mx - canvasScreenX());
        int py   = (int)(my - canvasScreenY());
        int half = brushSize / 2;
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