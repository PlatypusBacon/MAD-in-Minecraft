package bunger.group.client.tyler3;

import bunger.group.tyler3.entity.PlatformEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class PlatformEntityRenderer extends EntityRenderer<PlatformEntity, PlatformEntityRenderer.PlatformRenderState> {

    private static final Identifier PLATFORM_TEXTURE =
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "textures/entity/platform.png");

    private final BlockModelResolver blockModelResolver;

    public PlatformEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockModelResolver = context.getBlockModelResolver();
    }

    public static class PlatformRenderState extends EntityRenderState {
        public final BlockModelRenderState blockModelRenderState = new BlockModelRenderState();
        public boolean isCarrier = false;
    }

    @Override
    public PlatformRenderState createRenderState() {
        return new PlatformRenderState();
    }

    @Override
    public void extractRenderState(PlatformEntity entity, PlatformRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isCarrier = entity.isCarrier();

        BlockState blockState = entity.isCarrier()
                ? Blocks.IRON_BLOCK.defaultBlockState() // placeholder for carrier
                : entity.getCarriedState();

        if (!blockState.isAir()) {
            blockModelResolver.update(state.blockModelRenderState, blockState, BlockDisplayContext.create());
        } else {
            state.blockModelRenderState.clear();
        }
    }

    @Override
    public void submit(PlatformRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        if (!state.blockModelRenderState.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.5, -0.5, -0.5);
            state.blockModelRenderState.submit(
                    poseStack,
                    submitNodeCollector,
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    0
            );
            poseStack.popPose();
        }
    }
}