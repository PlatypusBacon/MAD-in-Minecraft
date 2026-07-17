package bunger.group.client.tyler3.dude;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.tyler3.entity.DudeEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;

public class DudeRenderer extends HumanoidMobRenderer<DudeEntity, DudeRenderState, DudeModel> {

private static final Identifier TEXTURE =
        Identifier.fromNamespaceAndPath(
                MutuallyAssuredDestruction.MOD_ID,
                "textures/entity/test.png"
        );

public DudeRenderer(EntityRendererProvider.Context context) {
    super(
            context,
            new DudeModel(context.bakeLayer(ModEntityModelLayers.DUDE)),
            0.5f
    );
    this.addLayer(new HumanoidArmorLayer<>(
            this,
            new ArmorModelSet<>(
                    new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_ARMOR.head())),
                    new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_ARMOR.chest())),
                    new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_ARMOR.legs())),
                    new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE_ARMOR.feet()))
            ),
            context.getEquipmentRenderer()
    ));

}

@Override
public DudeRenderState createRenderState() {
    return new DudeRenderState();
}

@Override
public Identifier getTextureLocation(DudeRenderState state) {
    return TEXTURE;
}

@Override
public void extractRenderState(DudeEntity entity, DudeRenderState state, float tickDelta) {
    super.extractRenderState(entity, state, tickDelta);
}
}