package bunger.group.client.csmit863.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class OverseerEntityModel extends EntityModel<OverseerRenderState> {
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    //:::dancing_animation
    public OverseerEntityModel(ModelPart root) {
        //:::dancing_animation
        super(root);
        head = root.getChild(PartNames.HEAD);
        leftLeg = root.getChild(PartNames.LEFT_LEG);
        rightLeg = root.getChild(PartNames.RIGHT_LEG);
    }

    //:::dancing_animation
    @Override
    public void setupAnim(OverseerRenderState state) {
        super.setupAnim(state);

        head.xRot = state.xRot * Mth.RAD_TO_DEG;
        head.yRot = state.yRot * Mth.RAD_TO_DEG;
        float limbSwingAmplitude = state.walkAnimationSpeed;
        float limbSwingAnimationProgress = state.walkAnimationPos;
        leftLeg.xRot = Mth.cos(limbSwingAnimationProgress * 0.2f + Mth.PI) * 1.4f * limbSwingAmplitude;
        rightLeg.xRot = Mth.cos(limbSwingAnimationProgress * 0.2f) * 1.4f * limbSwingAmplitude;
    }
    //:::dancing_animation

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();
        // Body
        root.addOrReplaceChild(PartNames.BODY,
                CubeListBuilder.create().texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4),
                PartPose.offset(0, 6, 0)
        );


        return LayerDefinition.create(modelData, 64, 32);
    }
}