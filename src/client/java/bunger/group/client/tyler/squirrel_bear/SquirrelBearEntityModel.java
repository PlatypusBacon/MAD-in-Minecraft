package bunger.group.client.tyler.squirrel_bear;

import bunger.group.client.tyler.squirrel.SquirrelEntityRenderState;
import bunger.group.tyler.entity.SquirrelBearEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SquirrelBearEntityModel extends EntityModel<SquirrelBearEntityRenderState> {

    private final ModelPart bb_main;

    public SquirrelBearEntityModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshData = new MeshDefinition();
        PartDefinition partData = meshData.getRoot();

        PartDefinition bb_main = partData.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(38, 31).addBox(0.0F, -8.2848F, -8.983F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE)
                        .texOffs(38, 37).addBox(0.0F, -5.0F, -9.0F, 3.0F, 2.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1",
                CubeListBuilder.create()
                        .texOffs(18, 46).addBox(-1.5F, -1.0F, -1.1F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-0.5F, -4.5F, -8.5F, -0.562F, 0.2086F, 1.5693F));

        bb_main.addOrReplaceChild("cube_r2",
                CubeListBuilder.create()
                        .texOffs(18, 46).addBox(-1.5F, -1.0F, -1.1F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -3.5F, -9.5F, -0.562F, 0.2086F, 1.5693F));

        bb_main.addOrReplaceChild("cube_r3",
                CubeListBuilder.create()
                        .texOffs(18, 46).addBox(-1.0F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.5F, -7.5F, -8.5F, -0.7854F, 0.0F, 0.7854F));

        bb_main.addOrReplaceChild("cube_r4",
                CubeListBuilder.create()
                        .texOffs(18, 46).addBox(-1.0F, -0.5F, -0.1F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -7.5F, -8.5F, -0.7854F, 0.0F, 0.7854F));

        bb_main.addOrReplaceChild("cube_r5",
                CubeListBuilder.create()
                        .texOffs(0, 46).addBox(-0.5F, -0.5F, -0.5F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(3.0F, -8.7848F, -6.483F, 0.0F, 0.0F, 0.8727F));

        bb_main.addOrReplaceChild("cube_r6",
                CubeListBuilder.create()
                        .texOffs(40, 43).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 1.0F, 3.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -8.7848F, -6.483F, 0.0F, 0.0F, -0.8727F));

        bb_main.addOrReplaceChild("cube_r7",
                CubeListBuilder.create()
                        .texOffs(14, 40).addBox(-1.5F, -0.5F, -2.5F, 2.0F, 1.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(3.5F, -7.7848F, -6.483F, 0.0F, 0.4363F, 0.0F));

        bb_main.addOrReplaceChild("cube_r8",
                CubeListBuilder.create()
                        .texOffs(0, 40).addBox(-0.5F, -0.5F, -2.5F, 2.0F, 1.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-0.5F, -7.7848F, -6.483F, 0.0F, -0.4363F, 0.0F));

        bb_main.addOrReplaceChild("cube_r9",
                CubeListBuilder.create()
                        .texOffs(32, 0).addBox(-1.0F, -1.0F, -3.5F, 2.0F, 2.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(3.0F, -5.0825F, -7.218F, 0.4996F, 0.27F, 0.1446F));

        bb_main.addOrReplaceChild("cube_r10",
                CubeListBuilder.create()
                        .texOffs(20, 31).addBox(-1.0F, -1.0F, -3.5F, 2.0F, 2.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0F, -5.0825F, -7.218F, 0.4996F, -0.27F, -0.1446F));

        bb_main.addOrReplaceChild("cube_r11",
                CubeListBuilder.create()
                        .texOffs(0, 31).addBox(-1.5F, -0.5F, -3.5F, 3.0F, 2.0F, 7.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -5.526F, -7.4489F, 0.48F, 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r12",
                CubeListBuilder.create()
                        .texOffs(28, 40).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.0F, -7.0F, 10.0F, 0.3054F, 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r13",
                CubeListBuilder.create()
                        .texOffs(26, 16).addBox(-0.5F, -2.0F, -5.5F, 2.0F, 4.0F, 11.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-1.0F, -4.5F, 0.5F, 0.0F, 0.0F, -0.2182F));

        bb_main.addOrReplaceChild("cube_r14",
                CubeListBuilder.create()
                        .texOffs(0, 16).addBox(-1.5F, -2.0F, -5.5F, 2.0F, 4.0F, 11.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(4.0F, -4.5F, 0.5F, 0.0F, 0.0F, 0.2182F));

        bb_main.addOrReplaceChild("cube_r15",
                CubeListBuilder.create()
                        .texOffs(28, 43).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.0F, -8.0F, 8.0F, 0.2618F, 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r16",
                CubeListBuilder.create()
                        .texOffs(14, 46).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.5F, -9.9384F, -4.7044F, 1.4878F, -0.6699F, -1.2343F));

        bb_main.addOrReplaceChild("cube_r17",
                CubeListBuilder.create()
                        .texOffs(10, 46).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.5F, -9.9384F, -4.7044F, 1.2019F, -0.4828F, -1.6132F));

        bb_main.addOrReplaceChild("cube_r18",
                CubeListBuilder.create()
                        .texOffs(32, 9).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 1.0F, 5.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -8.9142F, -6.0F, 0.2618F, 0.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r19",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 5.0F, 11.0F, CubeDeformation.NONE),
                PartPose.offsetAndRotation(1.5F, -6.5F, 1.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshData, 64, 64);
    }


}