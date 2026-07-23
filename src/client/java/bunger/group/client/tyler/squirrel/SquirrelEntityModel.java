package bunger.group.client.tyler.squirrel;


import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SquirrelEntityModel extends EntityModel<SquirrelEntityRenderState> {

    private final ModelPart bb_main;

    public SquirrelEntityModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshData = new MeshDefinition();
        PartDefinition partData = meshData.getRoot();

        PartDefinition bb_main = partData.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(24, 22).addBox(0.5F, -10.0F, -8.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(24, 15).addBox(0.5F, -2.5F, -5.0F, 2.0F, 2.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(-2.5F, -2.5F, -15.0F, 5.0F, 5.0F, 10.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -6.5F, 11.0F, 0.0F, 0.0F, -0.7854F));

        bb_main.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(30, 3).addBox(0.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -9.0F, -6.0F, -1.6352F, 0.0589F, -2.4017F));

        bb_main.addOrReplaceChild("cube_r3",
                CubeListBuilder.create()
                        .texOffs(30, 0).addBox(0.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.0F, -9.0F, -6.0F, -1.6352F, 0.0589F, -2.4017F));

        bb_main.addOrReplaceChild("cube_r4",
                CubeListBuilder.create()
                        .texOffs(24, 28).addBox(0.5F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -6.0F, -6.0F, 0.3491F, 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r5",
                CubeListBuilder.create()
                        .texOffs(12, 24).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-1.5562F, -2.9901F, -2.5F, 0.0F, 0.0F, 0.3054F));

        bb_main.addOrReplaceChild("cube_r6",
                CubeListBuilder.create()
                        .texOffs(0, 24).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(0, 15).addBox(-1.5F, -3.0F, 5.5F, 3.0F, 6.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.4438F, -2.9901F, -2.5F, 0.0F, 0.0F, -0.3491F));

        bb_main.addOrReplaceChild("cube_r7",
                CubeListBuilder.create()
                        .texOffs(12, 15).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 6.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-1.0216F, -3.3842F, 4.5F, 0.0F, 0.0F, 0.3491F));

        return LayerDefinition.create(meshData, 64, 64);
    }

}