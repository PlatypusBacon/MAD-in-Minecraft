package bunger.group.client.tyler3.shopping_cart;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;

public class ShoppingCartModel extends EntityModel<MinecartRenderState> {

    public ShoppingCartModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition front = root.addOrReplaceChild("front",
                CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-8.0F, -13.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, -8.0F, 0.0F, 3.1416F, 0.0F));

        front.addOrReplaceChild("bone",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        root.addOrReplaceChild("left",
                CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-8.0F, -13.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(-7.0F, 4.0F, 1.0F, 0.0F, -1.5708F, 0.0F));

        root.addOrReplaceChild("right",
                CubeListBuilder.create()
                        .texOffs(36, 18).addBox(-8.0F, -13.0F, -1.0F, 16.0F, 8.0F, 2.0F),
                PartPose.offsetAndRotation(7.0F, 4.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

        root.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(36, 28).addBox(-8.0F, -13.0F, -1.0F,  16.0F, 8.0F, 2.0F)
                        .texOffs(16, 48).addBox(-8.0F,  -3.0F, -1.0F,   2.0F, 2.0F, 2.0F)
                        .texOffs(16, 42).addBox(-8.0F,  -1.0F, -19.5F,  2.0F, 3.0F, 3.0F)
                        .texOffs(0,  50).addBox(-8.0F,  -3.0F, -19.0F,  2.0F, 2.0F, 2.0F)
                        .texOffs(26, 46).addBox( 6.0F,  -1.0F,  -1.5F,  2.0F, 3.0F, 3.0F)
                        .texOffs(8,  50).addBox( 6.0F,  -3.0F,  -1.0F,  2.0F, 2.0F, 2.0F)
                        .texOffs(36, 46).addBox( 6.0F,  -1.0F, -19.5F,  2.0F, 3.0F, 3.0F)
                        .texOffs(16, 52).addBox( 6.0F,  -3.0F, -19.0F,  2.0F, 2.0F, 2.0F)
                        .texOffs(46, 46).addBox(-8.0F,  -1.0F,  -1.5F,  2.0F, 3.0F, 3.0F)
                        .texOffs(28, 38).addBox(-8.0F, -13.0F,   1.0F,  2.0F, 2.0F, 6.0F)
                        .texOffs(0,  42).addBox(-8.0F, -14.0F,   7.0F,  2.0F, 2.0F, 6.0F)
                        .texOffs(44, 0 ).addBox( 6.0F, -13.0F,   1.0F,  2.0F, 2.0F, 6.0F)
                        .texOffs(44, 8 ).addBox( 6.0F, -14.0F,   7.0F,  2.0F, 2.0F, 6.0F)
                        .texOffs(44, 38).addBox( 6.0F, -14.0F,   7.0F,  2.0F, 2.0F, 6.0F),
                PartPose.offset(0.0F, 4.0F, 10.0F));

        root.addOrReplaceChild("bottom",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-10.0F, -8.0F, 3.0F, 20.0F, 16.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, 0.0F, -1.5708F, 1.5708F));

        root.addOrReplaceChild("bb_main",
                CubeListBuilder.create()
                        .texOffs(0, 38).addBox(-6.0F, -16.0F, 21.0F, 12.0F, 2.0F, 2.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(MinecartRenderState state) {
        // Static model
    }
}