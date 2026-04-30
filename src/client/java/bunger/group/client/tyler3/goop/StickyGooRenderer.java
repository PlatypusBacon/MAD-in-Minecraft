// StickyGooRenderer.java
package bunger.group.client.tyler3.goop;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import bunger.group.tyler3.tools.ClientAttachmentCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class StickyGooRenderer {

    public static void register() {
        LevelRenderEvents.COLLECT_SUBMITS.register(StickyGooRenderer::onCollectSubmits);
    }

    private static void onCollectSubmits(LevelRenderContext ctx) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (StickyGooModel.isModelMissing()) return;

        BlockStateModel model = StickyGooModel.getModel();
        SubmitNodeCollector collector = ctx.submitNodeCollector();
        PoseStack poseStack = ctx.poseStack();
        Vec3 cam = ctx.levelState().cameraRenderState.pos;

        List<BlockStateModelPart> parts = new ArrayList<>();
        model.collectParts(mc.level.getRandom(), parts);
        if (parts.isEmpty()) return;

        int[] tintLayers = new int[0];
        BlockPos playerPos = mc.player.blockPosition();

        BlockPos.betweenClosed(
                playerPos.offset(-16, -16, -16),
                playerPos.offset(16, 16, 16)
        ).forEach(pos -> {
            Set<Direction> faces = ClientAttachmentCache.getFaces(pos);
            if (faces.isEmpty()) return;

            int packedLight = LevelRenderer.getLightCoords(mc.level, pos);

            for (Direction face : faces) {
                poseStack.pushPose();
                poseStack.translate(
                        pos.getX() - cam.x(),
                        pos.getY() - cam.y(),
                        pos.getZ() - cam.z()
                );
                applyFaceRotation(poseStack, face);

                collector.submitBlockModel(
                        poseStack,
                        RenderTypes.cutoutMovingBlock(),
                        parts,
                        tintLayers,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        0
                );

                poseStack.popPose();
            }
        });
    }

    private static void applyFaceRotation(PoseStack poseStack, Direction face) {
        poseStack.translate(0.5, 0.5, 0.5);
        switch (face) {
            case UP    -> poseStack.translate(0, 0.5, 0);
            case DOWN  -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(180));
                poseStack.translate(0, 0.5, 0);
            }
            case NORTH -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.translate(0, 0.5, 0);
            }
            case SOUTH -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
                poseStack.translate(0, 0.5, 0);
            }
            case EAST  -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
                poseStack.translate(0, 0.5, 0);
            }
            case WEST  -> {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
                poseStack.translate(0, 0.5, 0);
            }
        }
        poseStack.translate(-0.5, -0.5, -0.5);
    }
}