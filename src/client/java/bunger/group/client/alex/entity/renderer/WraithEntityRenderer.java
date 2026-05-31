package bunger.group.client.alex.entity.renderer;


import bunger.group.MutuallyAssuredDestruction;

import bunger.group.alex.entity.WraithEntity;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.model.WraithEntityModel;
import bunger.group.client.alex.entity.state.WraithEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class WraithEntityRenderer extends MobRenderer<WraithEntity, WraithEntityRenderState, WraithEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/wraith.png");

    public WraithEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithEntityModel(context.bakeLayer(ModEntityModelLayers.WRAITH)), 0.4f);
    }

    @Override
    public WraithEntityRenderState createRenderState() {
        return new WraithEntityRenderState();
    }

    @Override
    public void extractRenderState(WraithEntity entity, WraithEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        // nothing extra to extract - no custom animation states
    }

    @Override
    public Identifier getTextureLocation(WraithEntityRenderState state) {
        return TEXTURE;
    }
}