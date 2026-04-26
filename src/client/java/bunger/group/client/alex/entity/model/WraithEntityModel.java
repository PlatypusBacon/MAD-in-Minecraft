package bunger.group.client.alex.entity.model;

import bunger.group.client.alex.entity.state.WraithEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class WraithEntityModel extends EntityModel<WraithEntityRenderState> {

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;

    public WraithEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 7.0F, 0.0F));

        partdefinition.addOrReplaceChild("headwear", CubeListBuilder.create()
                        .texOffs(1, 1).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 7.0F, 0.0F));

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(38, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 7.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(1, 45).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 9.0F, 0.0F));

        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(17, 45).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 9.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(WraithEntityRenderState state) {
        super.setupAnim(state);

        head.yRot = state.yRot * Mth.DEG_TO_RAD;
        head.xRot = state.xRot * Mth.DEG_TO_RAD;
        headwear.yRot = head.yRot;
        headwear.xRot = head.xRot;

        float bob = Mth.sin(state.ageInTicks * 0.05f) * 0.15f;
        body.y += bob;
        head.y += bob;
        headwear.y += bob;
        left_arm.y += bob;
        right_arm.y += bob;

        left_arm.xRot = -1.2f + Mth.sin(state.ageInTicks * 0.04f) * 0.1f;
        right_arm.xRot = -1.2f + Mth.sin(state.ageInTicks * 0.04f + Mth.PI) * 0.1f;
    }
}