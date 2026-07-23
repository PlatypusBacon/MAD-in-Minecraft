package bunger.group.client.tyler.god;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class GodEntityModel extends EntityModel<GodEntityRenderState> {

    private final ModelPart bb_main;

    public GodEntityModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition bb_main = root.addOrReplaceChild("bb_main",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(20, 26)
                        .addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-3.6464F, -18.0607F, -0.5F, 0.0F, 0.0F, 0.3927F));

        bb_main.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(8, 26)
                        .addBox(-2.5F, -0.5F, -0.5F, 5.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.3536F, -18.0607F, -0.5F, 0.0F, 0.0F, -0.3491F));

        bb_main.addOrReplaceChild("cube_r3",
                CubeListBuilder.create().texOffs(0, 13)
                        .addBox(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -9.0F, 0.5F, 0.0F, -0.2182F, 0.2618F));

        bb_main.addOrReplaceChild("cube_r4",
                CubeListBuilder.create().texOffs(0, 26)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-5.0F, -12.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        bb_main.addOrReplaceChild("cube_r5",
                CubeListBuilder.create().texOffs(20, 20)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-7.0F, -15.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        bb_main.addOrReplaceChild("cube_r6",
                CubeListBuilder.create().texOffs(20, 4)
                        .addBox(-1.5F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-7.0F, -17.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        bb_main.addOrReplaceChild("cube_r7",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-1.5F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        bb_main.addOrReplaceChild("cube_r8",
                CubeListBuilder.create().texOffs(20, 14)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(6.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        bb_main.addOrReplaceChild("cube_r9",
                CubeListBuilder.create().texOffs(20, 8)
                        .addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        bb_main.addOrReplaceChild("cube_r10",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-2.5F, -9.0F, 0.5F, 0.0F, 0.1745F, -0.3054F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(GodEntityRenderState state) {}
}