package bunger.group.client.tyler3;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler3.gui.PaintOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import bunger.group.client.tyler3.gui.GuiUtils;
import bunger.group.client.mixin.AbstractContainerScreenAccessor;

@Environment(EnvType.CLIENT)
public final class MixinBeGone {
    /**
     * Registers all screen events required for {@link PaintOverlay}.
     * Call {@link #register()} once from your client entrypoint.
     */

    /** Weak map so overlays are GC'd when their screen is gone. */
    private static final Map<AbstractContainerScreen<?>, PaintOverlay> OVERLAYS =
            new WeakHashMap<>();

    private MixinBeGone() {}

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof AbstractContainerScreen<?>)) return;
            AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) screen;

            // Load saved pages for this world (no-op if already loaded for this session)
            PaintOverlay.loadForCurrentWorld();

            PaintOverlay overlay = new PaintOverlay();
            OVERLAYS.put(containerScreen, overlay);

            // Swap recipe book button with tome button
            List<AbstractWidget> widgets = Screens.getWidgets(screen);
            widgets.stream()
                    .filter(w -> w instanceof ImageButton
                            && GuiUtils.isRecipeButton(screen, (ImageButton) w))
                    .findFirst()
                    .ifPresent(w -> {
                        ImageButton original = (ImageButton) w;
                        WidgetSprites tomeSprites = new WidgetSprites(
                                Identifier.fromNamespaceAndPath(
                                        MutuallyAssuredDestruction.MOD_ID, "tome/button"),
                                Identifier.fromNamespaceAndPath(
                                        MutuallyAssuredDestruction.MOD_ID, "tome/button_highlighted")
                        );
                        ImageButton replacement = new ImageButton(
                                original.getX(), original.getY(),
                                original.getWidth(), original.getHeight(),
                                tomeSprites,
                                btn -> overlay.toggleVisible()
                        );
                        widgets.remove(w);
                        widgets.add(replacement);
                    });

            // Render overlay after screen renders
            ScreenEvents.afterExtract(screen).register((s, graphics, mouseX, mouseY, tickProgress) -> {
                PaintOverlay ov = OVERLAYS.get(s);
                if (ov == null || !ov.isVisible()) return;
                AbstractContainerScreenAccessor acc = (AbstractContainerScreenAccessor) s;
                ov.setPosition(acc.getLeftPos(), acc.getTopPos());
                ov.render(graphics);
            });

            // Mouse click
            ScreenMouseEvents.afterMouseClick(screen).register((s, event, consumed) -> {
                PaintOverlay ov = OVERLAYS.get(s);
                if (ov == null || !ov.isVisible()) return consumed;
                boolean handled = ov.mouseClicked(event.x(), event.y(), event.button());
                return consumed || handled;
            });

            // Mouse drag
            ScreenMouseEvents.afterMouseDrag(screen).register((s, event, horizontalAmount, verticalAmount, consumed) -> {
                PaintOverlay ov = OVERLAYS.get(s);
                if (ov == null || !ov.isVisible()) return consumed;
                boolean handled = ov.mouseDragged(event.x(), event.y(), horizontalAmount, verticalAmount);
                return consumed || handled;
            });

            // Mouse release
            ScreenMouseEvents.afterMouseRelease(screen).register((s, event, consumed) -> {
                PaintOverlay ov = OVERLAYS.get(s);
                if (ov == null || !ov.isVisible()) return consumed;
                boolean handled = ov.mouseReleased(event.x(), event.y(), event.button());
                return consumed || handled;
            });

            // Save and clean up on screen close
            ScreenEvents.remove(screen).register(s -> {
                PaintOverlay.saveForCurrentWorld();
                OVERLAYS.remove(s);
            });
        });
    }
}