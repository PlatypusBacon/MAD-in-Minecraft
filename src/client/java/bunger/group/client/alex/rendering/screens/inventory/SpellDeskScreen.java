package bunger.group.client.alex.rendering.screens.inventory;

import bunger.group.alex.spell.LearnSpellPacket;
import bunger.group.alex.block.entity.SpellDeskEntity;
import bunger.group.alex.menu.SpellDeskMenu;
import bunger.group.alex.spell.SpellDefinition;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpellDeskScreen extends AbstractContainerScreen<SpellDeskMenu> {

    private static final Identifier CONTAINER_TEXTURE =
            Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");

    // ═══════════════════════════════════════════════════════════════
    //  GUI DIMENSIONS
    //  248px wide:  8 pad | 20 vial | 8 gap | 56 ritual panel | 8 gap | 140 spell list | 8 pad
    //  Inventory:   9×18 = 162px  →  INV_X = (248-162)/2 = 43
    // ═══════════════════════════════════════════════════════════════
    private static final int GUI_WIDTH  = 248;

    // Upper: 16 title + 110 content = 126
    // Lower: 3 divider + 6 gap + 10 label + 54 rows + 4 gap + 18 hotbar + 7 pad = 102
    // Total: 228
    private static final int GUI_HEIGHT = 228;

    // Content band
    private static final int TITLE_H       = 16;
    private static final int CONTENT_H     = 110;
    private static final int CONTENT_TOP   = TITLE_H;                    // = 16
    private static final int CONTENT_BOT   = CONTENT_TOP + CONTENT_H;   // = 126

    // ── Mana vial (left column) ───────────────────────────────────
    private static final int VIAL_X   = 8;
    private static final int VIAL_Y   = CONTENT_TOP + 14;  // = 30, leaves room for "Eitr" label above
    private static final int VIAL_W   = 12;
    private static final int VIAL_H   = 72;                // bar fill height

    // ── Ritual panel (centre column) ──────────────────────────────
    // x = 8 + 20 + 8 = 36   (vial_x + vial_w + gap_left_of_vial_border(4) + gap)
    private static final int RITUAL_X  = 36;
    private static final int RITUAL_W  = 56;
    private static final int RITUAL_Y  = CONTENT_TOP + 2;
    private static final int RITUAL_H  = CONTENT_H - 4;

    // Scroll slot — top of ritual panel.  MUST match menu slot 0.
    private static final int SCROLL_SLOT_X = 56;   // centred: RITUAL_X(36) + RITUAL_W/2(28) - 8 = 56
    private static final int SCROLL_SLOT_Y = 36;

    // Eitr slot — below arrow.  MUST match menu slot 1.
    private static final int MANA_SLOT_X   = 56;
    private static final int MANA_SLOT_Y   = 89;

    // ── Spell list (right column) ─────────────────────────────────
    private static final int LIST_X   = RITUAL_X + RITUAL_W + 8;  // = 100
    private static final int LIST_Y   = CONTENT_TOP + 4;
    private static final int LIST_W   = GUI_WIDTH - LIST_X - 8;   // = 140
    private static final int ENTRY_H  = 26;
    private static final int N_SPELLS = 4;   // 4 × 26 = 104 ≤ CONTENT_H(110) ✓

    // Max pixel width for spell name text before truncating
    private static final int NAME_MAX_W = 80;

    // ── Inventory ─────────────────────────────────────────────────
    private static final int INV_X       = (GUI_WIDTH - 162) / 2;   // = 43 (centred)
    private static final int INV_LABEL_Y = CONTENT_BOT + 9;         // = 135
    private static final int INV_Y       = INV_LABEL_Y + 10;        // = 145

    private int scrollOffset    = 0;
    private int hoveredSpellIdx = -1;

    public SpellDeskScreen(SpellDeskMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, GUI_WIDTH, GUI_HEIGHT);
    }

    // We only want "Spells" in the title bar, not the block entity name ("Spell Table").
    // Suppress vanilla title by pushing titleLabelX off-screen; draw our own in extractBackground.
    private static final String CUSTOM_TITLE = "— Spells —";

    @Override
    protected void init() {
        super.init();
        this.titleLabelX     = -9999;  // off-screen — suppresses vanilla title render
        this.titleLabelY     = 5;
        this.inventoryLabelX = INV_X;
        this.inventoryLabelY = INV_LABEL_Y;
    }

    // ═══════════════════════════════════════════════════════════════
    //  BACKGROUND
    // ═══════════════════════════════════════════════════════════════
    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int ox = this.leftPos;
        int oy = this.topPos;

        // Required for slot hit-testing
        graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, ox, oy, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);

        // ── Outer shell ───────────────────────────────────────────
        // 2-layer border: dark wood outer, warm inner
        graphics.fill(ox, oy, ox + GUI_WIDTH, oy + GUI_HEIGHT, 0xFF1A1208);
        graphics.fill(ox + 2, oy + 2, ox + GUI_WIDTH - 2, oy + GUI_HEIGHT - 2, 0xFF2E2010);
        graphics.fill(ox + 3, oy + 3, ox + GUI_WIDTH - 3, oy + GUI_HEIGHT - 3, 0xFF8B7355);

        // Corner ornaments (2×2 dark gems at each corner)
        int[] cx = {ox + 3, ox + GUI_WIDTH - 5};
        int[] cy = {oy + 3, oy + GUI_HEIGHT - 5};
        for (int x : cx) for (int y : cy) {
            graphics.fill(x, y, x + 2, y + 2, 0xFF2A1F0E);
        }

        // ── Title bar ─────────────────────────────────────────────
        graphics.fill(ox + 3, oy + 3, ox + GUI_WIDTH - 3, oy + TITLE_H + 1, 0xFF3A2A12);
        // Title bar bottom rule: dark / gold / dark
        graphics.fill(ox + 3, oy + TITLE_H - 1, ox + GUI_WIDTH - 3, oy + TITLE_H,     0xFF1A1208);
        graphics.fill(ox + 3, oy + TITLE_H,     ox + GUI_WIDTH - 3, oy + TITLE_H + 1, 0xFFB8960A);
        graphics.fill(ox + 3, oy + TITLE_H + 1, ox + GUI_WIDTH - 3, oy + TITLE_H + 2, 0xFF1A1208);

        // ── Content area background ───────────────────────────────
        graphics.fill(ox + 3, oy + CONTENT_TOP + 2, ox + GUI_WIDTH - 3, oy + CONTENT_BOT, 0xFF7A6648);

        // Subtle inner shadow top
        graphics.fill(ox + 3, oy + CONTENT_TOP + 2, ox + GUI_WIDTH - 3, oy + CONTENT_TOP + 4, 0x33000000);

        // ── Mana vial ─────────────────────────────────────────────
        drawManaVial(graphics, ox, oy);

        // ── Ritual panel ──────────────────────────────────────────
        drawRitualPanel(graphics, ox, oy);

        // ── Spell list ────────────────────────────────────────────
        drawSpellList(graphics, ox, oy, mouseX, mouseY);

        // ── Divider ───────────────────────────────────────────────
        int dY = oy + CONTENT_BOT;
        graphics.fill(ox + 3, dY,     ox + GUI_WIDTH - 3, dY + 1, 0xFF1A1208);
        graphics.fill(ox + 3, dY + 1, ox + GUI_WIDTH - 3, dY + 2, 0xFFB8960A);
        graphics.fill(ox + 3, dY + 2, ox + GUI_WIDTH - 3, dY + 3, 0xFF6A5030);
        graphics.fill(ox + 3, dY + 3, ox + GUI_WIDTH - 3, dY + 4, 0xFFB8960A);
        graphics.fill(ox + 3, dY + 4, ox + GUI_WIDTH - 3, dY + 5, 0xFF1A1208);

        // ── Inventory backgrounds ─────────────────────────────────
        drawInventoryPanels(graphics, ox, oy);
    }

    // ─────────────────────────────────────────────────────────────
    private void drawManaVial(GuiGraphicsExtractor g, int ox, int oy) {
        int bx = ox + VIAL_X + 4;   // left edge of actual vial (leaving room for border)
        int by = oy + VIAL_Y;
        int storedMana  = menu.getStoredMana();
        int clamped     = Math.max(0, Math.min(storedMana, SpellDeskEntity.MAX_MANA));
        int filledH     = SpellDeskEntity.MAX_MANA > 0
                ? (int)((clamped / (float) SpellDeskEntity.MAX_MANA) * VIAL_H)
                : 0;

        // Outer glow border (purple aura)
        g.fill(bx - 3, by - 3, bx + VIAL_W + 3, by + VIAL_H + 3, 0x443300AA);
        // Vial border layers
        g.fill(bx - 2, by - 2, bx + VIAL_W + 2, by + VIAL_H + 2, 0xFF1A0A2A);
        g.fill(bx - 1, by - 1, bx + VIAL_W + 1, by + VIAL_H + 1, 0xFF6B20CC);
        // Empty track
        g.fill(bx, by, bx + VIAL_W, by + VIAL_H, 0xFF08000F);

        // Fill (bottom-up)
        if (filledH > 0) {
            g.fillGradient(
                    bx, by + VIAL_H - filledH, bx + VIAL_W, by + VIAL_H,
                    0xFF00EEFF,   // bright cyan at top of fill
                    0xFF6600CC    // deep violet at bottom
            );
            // Inner shine stripe (left 2px of fill, lighter)
            g.fill(bx + 1, by + VIAL_H - filledH, bx + 3, by + VIAL_H, 0x5588FFFF);
        }
        // Shimmer line at fill surface
        if (filledH > 1) {
            g.fill(bx, by + VIAL_H - filledH, bx + VIAL_W, by + VIAL_H - filledH + 1, 0xFFDDF8FF);
        }

        // Tick marks on vial (4 marks at 25% intervals)
        for (int t = 1; t <= 3; t++) {
            int ty = by + VIAL_H - (VIAL_H * t / 4);
            g.fill(bx + VIAL_W, ty, bx + VIAL_W + 2, ty + 1, 0xFF6B20CC);
        }

        // "Eitr" label — sits at CONTENT_TOP+3, always above the bar (VIAL_Y=CONTENT_TOP+14)
        String topLabel = "Eitr";
        int labelX = bx + VIAL_W / 2 - font.width(topLabel) / 2;
        int labelY = oy + CONTENT_TOP + 3;  // guaranteed above bar, never clips title bar
        g.text(font, topLabel, labelX, labelY, 0xFFAADDFF, true);

        // Stacked mana number below vial:
        //   line 1: clamped value    e.g. "42"
        //   line 2: "/" + MAX        e.g. "/100"
        // Both lines centred on the vial
        String numLine1 = String.valueOf(clamped);
        String numLine2 = "/" + SpellDeskEntity.MAX_MANA;
        int numX1 = bx + VIAL_W / 2 - font.width(numLine1) / 2;
        int numX2 = bx + VIAL_W / 2 - font.width(numLine2) / 2;
        int numY1 = by + VIAL_H + 3;
        int numY2 = numY1 + font.lineHeight;
        // Clamp: don't go below content bottom
        if (numY2 + font.lineHeight > oy + CONTENT_BOT - 1) {
            numY1 = oy + CONTENT_BOT - 1 - font.lineHeight * 2;
            numY2 = numY1 + font.lineHeight;
        }
        g.text(font, numLine1, numX1, numY1, 0xFFAADDFF, true);
        g.text(font, numLine2, numX2, numY2, 0xFF7799CC, true);
    }

    // ─────────────────────────────────────────────────────────────
    private void drawRitualPanel(GuiGraphicsExtractor g, int ox, int oy) {
        int rx = ox + RITUAL_X;
        int ry = oy + RITUAL_Y;

        // Panel background — slightly darker parchment
        g.fill(rx, ry, rx + RITUAL_W, ry + RITUAL_H, 0xFF5C4A2A);
        g.fill(rx + 1, ry + 1, rx + RITUAL_W - 1, ry + RITUAL_H - 1, 0xFF6B5A3E);
        // Inner border glow
        g.fill(rx, ry, rx + RITUAL_W, ry + 1, 0xFF8B6A28);
        g.fill(rx, ry + RITUAL_H - 1, rx + RITUAL_W, ry + RITUAL_H, 0xFF4A3A1A);
        g.fill(rx, ry, rx + 1, ry + RITUAL_H, 0xFF8B6A28);
        g.fill(rx + RITUAL_W - 1, ry, rx + RITUAL_W, ry + RITUAL_H, 0xFF4A3A1A);

        // ── Scroll slot (top) ─────────────────────────────────────
        drawSlotBg(g, ox + SCROLL_SLOT_X, oy + SCROLL_SLOT_Y, 0xFF4B1A8A);
        drawScrollSilhouette(g, ox + SCROLL_SLOT_X, oy + SCROLL_SLOT_Y);

        // Dotted mystical channel between slots (no arrowhead)
        int dotTopY  = oy + SCROLL_SLOT_Y + 16 + 3;
        int dotBotY  = oy + MANA_SLOT_Y - 3;
        int dotCentX = ox + SCROLL_SLOT_X + 8;
        for (int dotY = dotTopY; dotY < dotBotY; dotY += 4) {
            int dotColor = ((dotY - dotTopY) / 4) % 2 == 0 ? 0xFFAA44FF : 0xFF6600CC;
            g.fill(dotCentX - 1, dotY, dotCentX + 1, dotY + 2, dotColor);
        }

        // ── Eitr slot (bottom) ────────────────────────────────────
        drawSlotBg(g, ox + MANA_SLOT_X, oy + MANA_SLOT_Y, 0xFF1A3A6A);
        drawDropletSilhouette(g, ox + MANA_SLOT_X, oy + MANA_SLOT_Y);
    }

    // ─────────────────────────────────────────────────────────────
    private void drawSpellList(GuiGraphicsExtractor g, int ox, int oy, int mouseX, int mouseY) {
        int lx     = ox + LIST_X;
        int ly     = oy + LIST_Y;
        int listH  = N_SPELLS * ENTRY_H;

        // Panel — dark tome page
        g.fill(lx - 2, ly - 2, lx + LIST_W + 2, ly + listH + 2, 0xFF1A1208);
        g.fill(lx - 1, ly - 1, lx + LIST_W + 1, ly + listH + 1, 0xFF2A1A44);
        g.fill(lx,     ly,     lx + LIST_W,     ly + listH,     0xFF10101E);
        // Top rule
        g.fill(lx, ly - 1, lx + LIST_W, ly, 0xFF6600CC);

        // "Spells" header
        String header = "— Spells —";
        g.text(font, header, lx + LIST_W / 2 - font.width(header) / 2, ly - 12, 0xFFDDCCAA, true);

        List<SpellDefinition> spells = menu.getAvailableSpells();
        hoveredSpellIdx = -1;

        if (spells.isEmpty()) {
            String h1 = menu.getScrollTypeOrdinal() < 0 ? "Insert a scroll" : "No spells found";
            String h2 = menu.getScrollTypeOrdinal() < 0 ? "to see spells"   : "for this scroll";
            g.text(font, h1, lx + LIST_W/2 - font.width(h1)/2, ly + listH/2 - font.lineHeight, 0xFF4A4A66, false);
            g.text(font, h2, lx + LIST_W/2 - font.width(h2)/2, ly + listH/2 + 2,               0xFF4A4A66, false);
        } else {
            for (int i = 0; i < N_SPELLS; i++) {
                int si = i + scrollOffset;
                if (si >= spells.size()) break;

                SpellDefinition spell    = spells.get(si);
                int ey                   = ly + i * ENTRY_H;
                boolean canAfford        = menu.getStoredMana() >= spell.manaCost();
                boolean hovered          = mouseX >= lx && mouseX < lx + LIST_W
                        && mouseY >= ey && mouseY < ey + ENTRY_H;
                if (hovered) hoveredSpellIdx = si;

                // Row background
                g.fill(lx, ey, lx + LIST_W, ey + ENTRY_H,
                        i % 2 == 0 ? 0xFF10101E : 0xFF13132A);

                // Hover tint
                if (hovered)
                    g.fill(lx, ey, lx + LIST_W, ey + ENTRY_H,
                            canAfford ? 0x3300BBEE : 0x33AA2222);

                // Row separator
                if (i > 0) g.fill(lx, ey, lx + LIST_W, ey + 1, 0xFF1E1E3A);

                // Affordability indicator bar (left edge, full row height)
                g.fill(lx, ey, lx + 3, ey + ENTRY_H, canAfford ? 0xFF00BB66 : 0xFF882222);

                // Item icon
                g.item(new ItemStack(spell.result()), lx + 5, ey + ENTRY_H / 2 - 8);

                // Spell name — truncated with ellipsis if too wide
                String name = truncate(spell.name(), NAME_MAX_W);
                int nameY = ey + ENTRY_H / 2 - font.lineHeight / 2 - 2;
                g.text(font, name, lx + 23, nameY, canAfford ? 0xFFE8E8CC : 0xFF886655, false);

                // Mana cost line — below name, right-aligned
                String cost = spell.manaCost() + " ✦";
                g.text(font, cost,
                        lx + LIST_W - font.width(cost) - 5,
                        ey + ENTRY_H - font.lineHeight - 3,
                        canAfford ? 0xFF5599CC : 0xFF553333, false);
            }
        }

        // Scroll arrows — in the header gutter, right-aligned
        if (scrollOffset > 0)
            g.text(font, "▲", lx + LIST_W - font.width("▲") - 2, ly - 12, 0xFFAA88CC, false);
        if (!spells.isEmpty() && scrollOffset + N_SPELLS < spells.size())
            g.text(font, "▼", lx + LIST_W - font.width("▼") - 2, ly + listH + 3, 0xFFAA88CC, false);
    }

    // ─────────────────────────────────────────────────────────────
    private void drawInventoryPanels(GuiGraphicsExtractor g, int ox, int oy) {
        // Inventory slots: x=INV_X, 9×18=162 wide.  3 rows: y=INV_Y to INV_Y+54
        // Hotbar: y = INV_Y + 58  to  INV_Y + 76
        int ix1 = ox + INV_X - 2;
        int ix2 = ox + INV_X + 162 + 2;

        // Main rows panel
        g.fill(ix1, oy + INV_Y - 2,     ix2, oy + INV_Y + 54 + 2, 0xFF5C4A2A);
        g.fill(ix1 + 1, oy + INV_Y - 1, ix2 - 1, oy + INV_Y + 54 + 1, 0xFF6B5A3E);

        // Hotbar panel (slightly lighter shade)
        g.fill(ix1, oy + INV_Y + 58,     ix2, oy + INV_Y + 76,     0xFF5C4A2A);
        g.fill(ix1 + 1, oy + INV_Y + 59, ix2 - 1, oy + INV_Y + 75, 0xFF6B5A3E);

        // Separator between rows and hotbar — gold rule
        g.fill(ix1, oy + INV_Y + 55, ix2, oy + INV_Y + 57, 0xFF1A1208);
        g.fill(ix1, oy + INV_Y + 56, ix2, oy + INV_Y + 57, 0xFF8B6A28);
    }

    // ─────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────

    /** Draws a slot background with a coloured accent border. */
    private void drawSlotBg(GuiGraphicsExtractor g, int sx, int sy, int accentColor) {
        g.fill(sx - 2, sy - 2, sx + 18, sy + 18, accentColor);
        g.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFF1A0A2A);
        g.fill(sx,     sy,     sx + 16, sy + 16, 0xFF2A2A2A);
    }

    /**
     * Truncates {@code text} so it renders no wider than {@code maxPx},
     * appending "…" if needed.
     */
    private String truncate(String text, int maxPx) {
        if (font.width(text) <= maxPx) return text;
        String ellipsis = "...";
        int ellipsisW = font.width(ellipsis);
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (font.width(sb.toString()) + font.width(String.valueOf(c)) + ellipsisW > maxPx) break;
            sb.append(c);
        }
        return sb + ellipsis;
    }

    // ═══════════════════════════════════════════════════════════════
    //  RENDER STATE + TOOLTIP
    // ═══════════════════════════════════════════════════════════════
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        if (hoveredSpellIdx >= 0) {
            List<SpellDefinition> spells = menu.getAvailableSpells();
            if (hoveredSpellIdx < spells.size()) {
                SpellDefinition spell      = spells.get(hoveredSpellIdx);
                ItemStack       resultItem = new ItemStack(spell.result());

                // Full vanilla item tooltip (name, lore, data components).
                // Only the base name shows for a plain new ItemStack — to include
                // custom lore/enchantments, have SpellDefinition carry a richer stack.
                List<Component> tip = new ArrayList<>(Screen.getTooltipFromItem(this.minecraft, resultItem));
                tip.add(Component.empty());
                tip.add(Component.literal("Cost: " + spell.manaCost() + " Eitr")
                        .withStyle(s -> s.withColor(0x5599CC)));
                if (menu.getStoredMana() < spell.manaCost()) {
                    tip.add(Component.literal("✗ Not enough Eitr")
                            .withStyle(s -> s.withColor(0xAA2222)));
                }
                graphics.setTooltipForNextFrame(font, tip, Optional.empty(), mouseX, mouseY);
            }
        } else {
            this.extractTooltip(graphics, mouseX, mouseY);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  INPUT
    // ═══════════════════════════════════════════════════════════════
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int lx = this.leftPos + LIST_X;
        int ly = this.topPos  + LIST_Y;
        if (mouseX >= lx && mouseX < lx + LIST_W
                && mouseY >= ly && mouseY < ly + N_SPELLS * ENTRY_H) {
            int max = Math.max(0, menu.getAvailableSpells().size() - N_SPELLS);
            scrollOffset = (int) Math.max(0, Math.min(max, scrollOffset - scrollY));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            int lx = this.leftPos + LIST_X;
            int ly = this.topPos  + LIST_Y;
            double mx = event.x(), my = event.y();
            List<SpellDefinition> spells = menu.getAvailableSpells();
            for (int i = 0; i < N_SPELLS; i++) {
                int si = i + scrollOffset;
                if (si >= spells.size()) break;
                int ey = ly + i * ENTRY_H;
                if (mx >= lx && mx < lx + LIST_W && my >= ey && my < ey + ENTRY_H) {
                    ClientPlayNetworking.send(new LearnSpellPacket(si));
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
    /**
     * Draws a simple scroll silhouette (outline only) centred in a 16×16 slot.
     * sx/sy = top-left of the 16px slot interior.
     */
    private void drawScrollSilhouette(GuiGraphicsExtractor g, int sx, int sy) {
        int c = 0x55AAAAAA;  // faint grey outline
        // Horizontal scroll lying on its side, centred in 16x16
        // Main tube body outline: 12px wide, 6px tall, centred at (sx+8, sy+8)
        int bx = sx + 1, by = sy + 6;
        int bw = 14, bh = 4;
        // Top edge
        g.fill(bx + 2, by,         bx + bw - 2, by + 1,         c);
        // Bottom edge
        g.fill(bx + 2, by + bh - 1, bx + bw - 2, by + bh,        c);
        // Left cap outline: semicircle approximated as 3 vertical steps
        g.fill(bx + 1, by,         bx + 2,       by + 1,         c); // top-left corner
        g.fill(bx,     by + 1,     bx + 1,       by + bh - 1,    c); // left side
        g.fill(bx + 1, by + bh - 1, bx + 2,      by + bh,        c); // bot-left corner
        // Right cap outline
        g.fill(bx + bw - 2, by,         bx + bw - 1, by + 1,         c); // top-right corner
        g.fill(bx + bw - 1, by + 1,     bx + bw,     by + bh - 1,    c); // right side
        g.fill(bx + bw - 2, by + bh - 1, bx + bw - 1, by + bh,       c); // bot-right corner
        // Rolled end ridge lines (short vertical marks just inside each cap)
        g.fill(bx + 2, by,     bx + 3, by + bh+2, c);
        g.fill(bx + bw - 3, by, bx + bw - 2, by + bh+2, c);
    }

    /**
     * Draws a simple droplet/vial silhouette centred in a 16×16 slot.
     * sx/sy = top-left of the 16px slot interior.
     */
    private void drawDropletSilhouette(GuiGraphicsExtractor g, int sx, int sy) {
        int c = 0x55AAAAAA;  // faint grey outline
        // Circle outline, ~10px diameter, centred at (sx+8, sy+8)
        // Approximated with horizontal spans per row:
        //   row offsets from centre y=8: -5,-4,-3,-2,-1,0,1,2,3,4,5
        //   corresponding half-widths:    2, 4, 4, 5, 5,5,5,5,4,4, 2
        int cx = sx + 8, cy = sy + 8;
        // Top cap (row -5)
        g.fill(cx - 2, cy - 5, cx + 2, cy - 4, c);
        // Upper rows (-4, -3): fill left+right 1px only
        g.fill(cx - 4, cy - 4, cx - 3, cy - 2, c);
        g.fill(cx + 3, cy - 4, cx + 4, cy - 2, c);
        // Wide rows (-2 to +2): left+right 1px edges only
        g.fill(cx - 5, cy - 2, cx - 4, cy + 3, c);
        g.fill(cx + 4, cy - 2, cx + 5, cy + 3, c);
        // Lower rows (+3, +4)
        g.fill(cx - 4, cy + 3, cx - 3, cy + 5, c);
        g.fill(cx + 3, cy + 3, cx + 4, cy + 5, c);
        // Bottom cap (row +5)
        g.fill(cx - 2, cy + 4, cx + 2, cy + 5, c);
    }


}