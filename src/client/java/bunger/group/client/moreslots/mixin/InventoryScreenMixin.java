package bunger.group.client.moreslots.mixin;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.moreslots.api.SlotTypes;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen {

    private static final int PANEL_SIZE = 24;
    private static final int SLOT_SIZE = 18;
    private static final int PADDING = 3;
    private static final int ARMOR_OFFSET_Y = 4;

    protected InventoryScreenMixin(AbstractContainerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void renderPanel(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        int slotCount = SlotTypes.SLOT_COUNT;
        int panelX = this.leftPos - PANEL_SIZE;
        int panelY = this.topPos + ARMOR_OFFSET_Y;

        for (int i = 0; i < slotCount; i++) {
            // background
            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/gui/extra_slots_panel.png"),
                    panelX, panelY + (i * PANEL_SIZE), 0.0F, 0.0F, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE, PANEL_SIZE);

            // slot sprite
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    Identifier.withDefaultNamespace("container/slot"),
                    panelX + PADDING, panelY + PADDING + (i * PANEL_SIZE), SLOT_SIZE, SLOT_SIZE);
        }
    }
}