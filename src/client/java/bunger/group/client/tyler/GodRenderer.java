package bunger.group.client.tyler;

import bunger.group.entity.GodEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GodRenderer extends MobRenderer<GodEntity, GodRenderer.GodModel<GodEntity>> {

    public static final ModelLayerLocation GOD_LAYER =
            new ModelLayerLocation(
                    new ResourceLocation("mutually-assured-destruction", "god"),
                    "main"
            );

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("mutually-assured-destruction", "textures/entity/god.png");

    public GodRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GodModel<>(ctx.bakeLayer(GOD_LAYER)), 0.5f);
    }

    @Override
    protected void scale(GodEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(6.0f, 6.0f, 6.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(GodEntity entity) {
        return TEXTURE;
    }

    public static class GodModel<T extends GodEntity> extends EntityModel<T> {

        private final ModelPart bb_main;

        public GodModel(ModelPart root) {
            this.bb_main = root.getChild("bb_main");
        }

        public static LayerDefinition createBodyLayer() {
            MeshDefinition mesh = new MeshDefinition();
            PartDefinition root = mesh.getRoot();

            PartDefinition bb_main = root.addOrReplaceChild("bb_main",
                    CubeListBuilder.create(),
                    PartPose.offset(0.0F, 24.0F, 0.0F));

            // cube_r1 — left arm upper segment
            bb_main.addOrReplaceChild("cube_r1",
                    CubeListBuilder.create().texOffs(20, 26)
                            .addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(-3.6464F, -18.0607F, -0.5F, 0.0F, 0.0F, 0.3927F));

            // cube_r2 — right arm upper segment
            bb_main.addOrReplaceChild("cube_r2",
                    CubeListBuilder.create().texOffs(8, 26)
                            .addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F),
                    PartPose.offsetAndRotation(2.3536F, -18.0607F, -0.5F, 0.0F, 0.0F, -0.3491F));

            // cube_r3 — right body half
            bb_main.addOrReplaceChild("cube_r3",
                    CubeListBuilder.create().texOffs(0, 13)
                            .addBox(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F),
                    PartPose.offsetAndRotation(1.5F, -9.0F, 0.5F, 0.0F, -0.2182F, 0.2618F));

            // cube_r4 — left lower arm
            bb_main.addOrReplaceChild("cube_r4",
                    CubeListBuilder.create().texOffs(0, 26)
                            .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                    PartPose.offsetAndRotation(-5.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

            // cube_r5 — left mid arm
            bb_main.addOrReplaceChild("cube_r5",
                    CubeListBuilder.create().texOffs(20, 20)
                            .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                    PartPose.offsetAndRotation(-7.0F, -15.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

            // cube_r6 — left upper arm
            bb_main.addOrReplaceChild("cube_r6",
                    CubeListBuilder.create().texOffs(20, 4)
                            .addBox(-1.5F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(-7.0F, -17.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

            // cube_r7 — right upper arm
            bb_main.addOrReplaceChild("cube_r7",
                    CubeListBuilder.create().texOffs(20, 0)
                            .addBox(-1.5F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F),
                    PartPose.offsetAndRotation(5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

            // cube_r8 — right mid arm
            bb_main.addOrReplaceChild("cube_r8",
                    CubeListBuilder.create().texOffs(20, 14)
                            .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                    PartPose.offsetAndRotation(6.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

            // cube_r9 — right lower arm
            bb_main.addOrReplaceChild("cube_r9",
                    CubeListBuilder.create().texOffs(20, 8)
                            .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                    PartPose.offsetAndRotation(4.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

            // cube_r10 — left body half
            bb_main.addOrReplaceChild("cube_r10",
                    CubeListBuilder.create().texOffs(0, 0)
                            .addBox(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F),
                    PartPose.offsetAndRotation(-2.5F, -9.0F, 0.5F, 0.0F, 0.1745F, -0.3054F));

            return LayerDefinition.create(mesh, 32, 32);
        }

        @Override
        public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                              float ageInTicks, float netHeadYaw, float headPitch) {}

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                                   int packedLight, int packedOverlay,
                                   float red, float green, float blue, float alpha) {
            bb_main.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }
}