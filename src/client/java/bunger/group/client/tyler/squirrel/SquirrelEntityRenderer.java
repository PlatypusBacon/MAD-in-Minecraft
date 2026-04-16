package bunger.group.client.tyler.squirrel;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SquirrelEntityRenderer extends MobRenderer<SquirrelEntity, SquirrelEntityRenderState, SquirrelEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/squirrel.png");

    public SquirrelEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SquirrelEntityModel(context.bakeLayer(ModEntityModelLayers.SQUIRREL)), 0.375f); // 0.375 shadow radius
    }

    @Override
    public SquirrelEntityRenderState createRenderState() {
        return new SquirrelEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(SquirrelEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(SquirrelEntity entity, SquirrelEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.xRot = entity.getXRot();
    }
}