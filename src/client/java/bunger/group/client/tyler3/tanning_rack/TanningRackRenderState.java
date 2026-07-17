package bunger.group.client.tyler3.tanning_rack;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class TanningRackRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] items = new ItemStackRenderState[6];
    public Direction facing = Direction.NORTH;

    public TanningRackRenderState() {
        for (int i = 0; i < items.length; i++) {
            items[i] = new ItemStackRenderState();
        }
    }
}