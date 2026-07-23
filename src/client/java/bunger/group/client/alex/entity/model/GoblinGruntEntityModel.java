package bunger.group.client.alex.entity.model;

import bunger.group.client.alex.entity.state.GoblinGruntEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class GoblinGruntEntityModel extends EntityModel<GoblinGruntEntityRenderState> implements ArmedModel<GoblinGruntEntityRenderState> {

    private final ModelPart head;
    private final ModelPart nose;
    private final ModelPart left_ear;
    private final ModelPart right_ear;
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

    public GoblinGruntEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.nose = this.head.getChild("nose");
        this.left_ear = this.head.getChild("left_ear");
        this.right_ear = this.head.getChild("right_ear");
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
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        head.addOrReplaceChild("nose",
                CubeListBuilder.create()
                        .texOffs(42, 34).addBox(-1.0F, -4.0F, -8.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 12).addBox(-1.0F, -5.0F, -7.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 1.0F, 1.0F));

        head.addOrReplaceChild("left_ear",
                        CubeListBuilder.create().texOffs(42, 40).addBox(0.0F, -0.6F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(4.5F, -3.0F, -1.0F, -0.48F, 0.5236F, -1.5708F))
                .addOrReplaceChild("left_ear_r1",
                        CubeListBuilder.create().texOffs(42, 24).addBox(2.5F, -25.6F, 1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-2.5F, 25.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        head.addOrReplaceChild("right_ear",
                        CubeListBuilder.create().texOffs(42, 40).addBox(0.0F, -0.6F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-3.5F, -3.0F, -1.0F, 0.48F, 0.5236F, -1.5708F)) // was -4.5F
                .addOrReplaceChild("left_ear_r2",
                        CubeListBuilder.create().texOffs(42, 24).addBox(2.5F, -25.6F, 1.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)),
                        PartPose.offsetAndRotation(-2.5F, 25.0F, 2.0F, 0.0F, 0.3491F, 0.0F));

        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(1, 17).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("jacket",
                CubeListBuilder.create().texOffs(44, 49).addBox(-3.0F, 8.0F, -1.5F, 6.0F, 11.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        // Arms: pivot at shoulder (y=8, top of body), box starts at y=0
        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(1, 29).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 8.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_sleeve",
                CubeListBuilder.create().texOffs(15, 29).addBox(-2.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(5.0F, 8.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(29, 29).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 8.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_sleeve",
                CubeListBuilder.create().texOffs(33, 1).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-5.0F, 8.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(1, 41).addBox(-2.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_pants",
                CubeListBuilder.create().texOffs(41, 13).addBox(-2.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(15, 41).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_pants",
                CubeListBuilder.create().texOffs(29, 41).addBox(-1.0F, 4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void translateToHand(GoblinGruntEntityRenderState state, HumanoidArm arm, PoseStack poseStack) {
        ModelPart handPart = arm == HumanoidArm.RIGHT ? right_arm : left_arm;
        handPart.translateAndRotate(poseStack);

        if (arm == HumanoidArm.RIGHT) {
            poseStack.translate(0.03125F, -0.1F, 0.03F);
        } else {
            poseStack.translate(-0.03125F, -0.1F, 0.03F);
        }
    }

    @Override
    public void setupAnim(GoblinGruntEntityRenderState state) {
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