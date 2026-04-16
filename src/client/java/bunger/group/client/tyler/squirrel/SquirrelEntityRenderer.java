package bunger.group.client.tyler.squirrel;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityRenderState;
import bunger.group.tyler.entity.SquirrelEntity;
import com.mojang.blaze3d.vertex.PoseStack;
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
    protected void scale(SquirrelEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public void extractRenderState(SquirrelEntity entity, SquirrelEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.xRot = entity.getXRot();
        state.ageScale = 4.0f;  // or 3.5f, etc

    }
}