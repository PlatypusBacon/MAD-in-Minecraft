package bunger.group.client.alex.entity.model;

import bunger.group.client.alex.entity.state.SkeletonRangerEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class SkeletonRangerEntityModel extends EntityModel<SkeletonRangerEntityRenderState> implements ArmedModel<SkeletonRangerEntityRenderState> {

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public SkeletonRangerEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 36).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.1F));

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.1F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void translateToHand(SkeletonRangerEntityRenderState state, HumanoidArm arm, PoseStack poseStack) {
        ModelPart handPart = arm == HumanoidArm.RIGHT ? right_arm : left_arm;
        handPart.translateAndRotate(poseStack);
    }

    @Override
    public void setupAnim(SkeletonRangerEntityRenderState state) {
        super.setupAnim(state);

        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        head.xRot = state.xRot * Mth.DEG_TO_RAD;

        float swing = Mth.sin(state.walkAnimPos * 0.6662F);
        float swingAmount = state.walkAnimSpeed * 0.5F;

        right_leg.xRot = -swing * swingAmount;
        left_leg.xRot  =  swing * swingAmount;

        float bowPull = state.bowDrawTime;
        if (bowPull > 0.0F) {
            right_arm.xRot = head.xRot - Mth.HALF_PI;
            right_arm.yRot = 0.0F;
            right_arm.zRot = bowPull * 0.1F;

            left_arm.xRot  = head.xRot - Mth.HALF_PI;
            left_arm.yRot  = (bowPull * 0.45F);
            left_arm.zRot  = bowPull * 0.2F;
        } else {
            right_arm.xRot =  swing * swingAmount;
            right_arm.yRot = 0.0F;
            right_arm.zRot = 0.0F;
            left_arm.xRot  = -swing * swingAmount;
            left_arm.yRot  = 0.0F;
            left_arm.zRot  = 0.0F;
        }
    }
}