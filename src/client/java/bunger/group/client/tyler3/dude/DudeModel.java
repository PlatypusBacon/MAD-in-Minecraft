package bunger.group.client.tyler3.dude;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class DudeModel extends HumanoidModel<DudeRenderState> {

    public DudeModel(ModelPart root) {
        super(root);
    }
    public static LayerDefinition getTexturedModelData() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f), 64, 64);
    }
}