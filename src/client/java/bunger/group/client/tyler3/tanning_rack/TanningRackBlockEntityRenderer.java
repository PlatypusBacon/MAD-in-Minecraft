package bunger.group.client.tyler3.tanning_rack;

import bunger.group.tyler2.block.TanningRackBlock;
import bunger.group.tyler2.entity.TanningRackBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TanningRackBlockEntityRenderer implements BlockEntityRenderer<TanningRackBlockEntity, TanningRackRenderState> {

    private final ItemModelResolver itemModelResolver;

    public TanningRackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public TanningRackRenderState createRenderState() {
        return new TanningRackRenderState();
    }

    @Override
    public void extractRenderState(
            TanningRackBlockEntity blockEntity,
            TanningRackRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        // Store facing so submit() can orient items correctly
        BlockState blockState = blockEntity.getBlockState();
        state.facing = blockState.getValue(TanningRackBlock.FACING);

        int baseSeed = (int) blockEntity.getBlockPos().asLong();
        for (int i = 0; i < TanningRackBlockEntity.SLOT_COUNT; i++) {
            state.items[i] = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(
                    state.items[i],
                    blockEntity.getItem(i),
                    ItemDisplayContext.NONE,
                    blockEntity.getLevel(),
                    null,
                    baseSeed + i
            );
        }
    }

    @Override
    public void submit(
            TanningRackRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        // The bar spans from x=1/16 to x=15/16 (for NORTH/SOUTH facing).
        // We space 6 items evenly across that 14/16 width.
        // For EAST/WEST the bar runs along Z, so we swap axes below.
        boolean alongX = state.facing == Direction.NORTH || state.facing == Direction.SOUTH;

        for (int i = 0; i < TanningRackBlockEntity.SLOT_COUNT; i++) {
            ItemStackRenderState item = state.items[i];
            if (item.isEmpty()) continue;

            // t goes 0→1 across the 6 slots, centred within each slot
            float t = (i + 0.5f) / TanningRackBlockEntity.SLOT_COUNT;

            // Map t into block-local bar coordinates (1px–15px → 0.0625–0.9375)
            float barOffset = 0.0625f + t * 0.875f;

            float localX = alongX ? barOffset : 0.5f;
            float localZ = alongX ? 0.5f : barOffset;
            float localY = 0.6f;

            poseStack.pushPose();
            poseStack.translate(localX, localY, localZ);

            if (!alongX) {
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            }

            poseStack.scale(0.3f, 0.3f, 0.3f);
            item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}