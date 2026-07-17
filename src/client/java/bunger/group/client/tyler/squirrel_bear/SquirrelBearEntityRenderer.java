package bunger.group.client.tyler.squirrel_bear;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.client.tyler.squirrel.SquirrelEntityRenderState;
import bunger.group.tyler.entity.SquirrelBearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;


public class SquirrelBearEntityRenderer extends MobRenderer<SquirrelBearEntity, SquirrelBearEntityRenderState, SquirrelBearEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/squirrel_bear.png");

    public SquirrelBearEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SquirrelBearEntityModel(context.bakeLayer(ModEntityModelLayers.SQUIRREL_BEAR)), 0.375f); // 0.375 shadow radius
    }

    @Override
    public SquirrelBearEntityRenderState createRenderState() {
        return new SquirrelBearEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(SquirrelBearEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(SquirrelBearEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(4.0f, 4.0f, 4.0f);
        poseStack.translate(0.0, 0.2, 0.0);
    }

    @Override
    public void extractRenderState(SquirrelBearEntity entity, SquirrelBearEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.xRot = entity.getXRot();
        state.ageScale = 2.5f;
    }
}