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
        // Body
        root.addOrReplaceChild(PartNames.BODY,
                CubeListBuilder.create().texOffs(16, 16).addBox(-4, 0, -2, 8, 12, 4),
                PartPose.offset(0, 6, 0)
        );
        // Head
        root.addOrReplaceChild(PartNames.HEAD,
                CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8),
                PartPose.offset(0, 6, 0)
        );
        // Left front leg
        root.addOrReplaceChild(PartNames.LEFT_LEG,
                CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 6, 4),
                PartPose.offset(-2, 18, 2)
        );
        // Right front leg
        root.addOrReplaceChild(PartNames.RIGHT_LEG,
                CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 6, 4),
                PartPose.offset(2, 18, 2)
        );
        // Left back leg
        root.addOrReplaceChild("left_hind_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 6, 4),
                PartPose.offset(-2, 18, -2)
        );
        // Right back leg
        root.addOrReplaceChild("right_hind_leg",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2, 0, -2, 4, 6, 4),
                PartPose.offset(2, 18, -2)
        );

        return LayerDefinition.create(modelData, 64, 32);
    }
}