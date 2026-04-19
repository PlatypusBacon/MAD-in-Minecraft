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

public class ShroomjakEntityModel extends EntityModel<ShroomjakRenderState> {
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    //:::dancing_animation
    public ShroomjakEntityModel(ModelPart root) {
        //:::dancing_animation
        super(root);
        head = root.getChild(PartNames.HEAD);
        leftLeg = root.getChild(PartNames.LEFT_LEG);
        rightLeg = root.getChild(PartNames.RIGHT_LEG);
    }

    //:::dancing_animation
    @Override
    public void setupAnim(ShroomjakRenderState state) {
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
        root.addOrReplaceChild(
                PartNames.BODY,
                CubeListBuilder.create().addBox(
                        /* x */ -6,
                        /* y */ -6,
                        /* z */ -6,
                        /* width */ 12,
                        /* height */ 12,
                        /* depth */ 12
                ),
                PartPose.offset(0, 8, 0)
        );
        root.addOrReplaceChild(
                PartNames.HEAD,
                CubeListBuilder.create().texOffs(36, 0).addBox(-3, -6, -3, 6, 6, 6),
                PartPose.offset(0, 2, 0)
        );
        root.addOrReplaceChild(
                PartNames.LEFT_LEG,
                CubeListBuilder.create().texOffs(48, 12).addBox(-2, 0, -2, 4, 10, 4),
                PartPose.offset(-2.5f, 14, 0)
        );
        root.addOrReplaceChild(
                PartNames.RIGHT_LEG,
                CubeListBuilder.create().texOffs(48, 12).addBox(-2, 0, -2, 4, 10, 4),
                PartPose.offset(2.5f, 14, 0)
        );
        return LayerDefinition.create(modelData, 64, 32);
    }
}