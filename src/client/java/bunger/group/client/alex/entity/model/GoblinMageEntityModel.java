package bunger.group.client.alex.entity.model;

import bunger.group.client.alex.entity.state.GoblinMageEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class GoblinMageEntityModel extends EntityModel<GoblinMageEntityRenderState> implements ArmedModel<GoblinMageEntityRenderState> {

    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart left_ear;
    private final ModelPart right_ear;
    private final ModelPart hat;
    private final ModelPart body;
    private final ModelPart jacket;
    private final ModelPart left_arm;
    private final ModelPart left_sleeve;
    private final ModelPart right_arm;
    private final ModelPart right_sleeve;
    private final ModelPart left_leg;
    private final ModelPart left_pants;
    private final ModelPart right_leg;
    private final ModelPart right_pants;

    public GoblinMageEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.nose = this.head.getChild("nose");
        this.left_ear = this.head.getChild("left_ear");
        this.right_ear = this.head.getChild("right_ear");
        this.hat = this.head.getChild("hat");
        this.body = root.getChild("body");
        this.jacket = root.getChild("jacket");
        this.left_arm = root.getChild("left_arm");
        this.left_sleeve = root.getChild("left_sleeve");
        this.right_arm = root.getChild("right_arm");
        this.right_sleeve = root.getChild("right_sleeve");
        this.left_leg = root.getChild("left_leg");
        this.left_pants = root.getChild("left_pants");
        this.right_leg = root.getChild("right_leg");
        this.right_pants = root.getChild("right_pants");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 11).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        head.addOrReplaceChild("nose",
                CubeListBuilder.create()
                        .texOffs(50, 21).addBox(-1.0F, -4.0F, -8.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(50, 33).addBox(-1.0F, -5.0F, -7.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 1.0F, 1.0F));

        head.addOrReplaceChild("left_ear",
                        CubeListBuilder.create().texOffs(50, 27).addBox(0.0F, -0.6F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(4.5F, -3.0F, -1.0F, -0.48F, 0.5236F, -1.5708F))
                .addOrReplaceChild("left_ear_r1",
                        CubeListBuilder.create().texOffs(50, 11).addBox(2.5F, -25.6F, 1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-2.5F, 25.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        head.addOrReplaceChild("right_ear",
                        CubeListBuilder.create().texOffs(50, 30).addBox(0.0F, -0.6F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-3.5F, -3.0F, -1.0F, 0.48F, 0.5236F, -1.5708F))
                .addOrReplaceChild("left_ear_r2",
                        CubeListBuilder.create().texOffs(50, 16).addBox(2.5F, -25.6F, 1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-2.5F, 25.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 27).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        hat.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(24, 37).addBox(-1.0F, -2.5F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -13.0F, 1.0F, -0.9599F, 0.0F, 0.0F));

        hat.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(32, 36).addBox(-4.5F, -2.0F, -1.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.0F, -11.0F, -1.0F, -0.5236F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(32, 25).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("jacket",
                CubeListBuilder.create().texOffs(69, 26).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 13.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(0, 37).addBox(-2.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_sleeve",
                CubeListBuilder.create().texOffs(12, 37).addBox(-2.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(40, 0).addBox(-1.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_sleeve",
                CubeListBuilder.create().texOffs(24, 45).addBox(-1.0F, 6.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(36, 45).addBox(-2.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_pants",
                CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(12, 48).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_pants",
                CubeListBuilder.create().texOffs(48, 45).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void translateToHand(GoblinMageEntityRenderState state, HumanoidArm arm, PoseStack poseStack) {
        ModelPart handPart = arm == HumanoidArm.RIGHT ? right_arm : left_arm;
        handPart.translateAndRotate(poseStack);

        if (arm == HumanoidArm.RIGHT) {
            poseStack.translate(0.03125F, 0.3F, 0.03F);
        } else {
            poseStack.translate(-0.03125F, 0.3F, 0.03F);
        }
    }

    @Override
    public void setupAnim(GoblinMageEntityRenderState state) {
        super.setupAnim(state);

        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        head.xRot = state.xRot * Mth.DEG_TO_RAD;

        float swing = Mth.sin(state.walkAnimPos * 0.6662F);
        float swingAmount = state.walkAnimSpeed * 0.5F;

        right_arm.xRot = swing * swingAmount;
        right_arm.zRot = 0.0F;
        left_arm.xRot = -swing * swingAmount;
        left_arm.zRot = 0.0F;
        right_sleeve.xRot = right_arm.xRot;
        right_sleeve.zRot = 0.0F;
        left_sleeve.xRot = left_arm.xRot;
        left_sleeve.zRot = 0.0F;

        right_leg.xRot = -swing * swingAmount;
        left_leg.xRot = swing * swingAmount;
        right_pants.xRot = right_leg.xRot;
        left_pants.xRot = left_leg.xRot;

        if (state.attackTime > 0.0F) {
            float attack = Mth.sin(state.attackTime * Mth.PI);
            right_arm.xRot -= attack * Mth.HALF_PI;
            right_arm.zRot  = attack * 0.3F;
            right_sleeve.xRot = right_arm.xRot;
            right_sleeve.zRot = right_arm.zRot;
        }
    }
}