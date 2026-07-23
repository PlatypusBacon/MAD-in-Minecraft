package bunger.group.client.tyler3.hot_plate;


import bunger.group.tyler2.entity.HotPlateBlockEntity;

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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class HotPlateBlockEntityRenderer implements BlockEntityRenderer<HotPlateBlockEntity, HotPlateRenderState> {

    private final ItemModelResolver itemModelResolver;

    public HotPlateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public HotPlateRenderState createRenderState() {
        return new HotPlateRenderState();
    }

    @Override
    public void extractRenderState(
            HotPlateBlockEntity blockEntity,
            HotPlateRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        int seed = (int) blockEntity.getBlockPos().asLong();
        state.item = new ItemStackRenderState();
        this.itemModelResolver.updateForTopItem(
                state.item,
                blockEntity.getItem(),
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                seed
        );
    }

    @Override
    public void submit(
            HotPlateRenderState state,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera
    ) {
        if (state.item.isEmpty()) return;

        poseStack.pushPose();

        poseStack.translate(0.5f, 0.24921875f, 0.5f); //most random ass number

        // Lay flat
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

        // Scale down to fit on the plate
        poseStack.scale(0.5f, 0.5f, 0.5f);

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}