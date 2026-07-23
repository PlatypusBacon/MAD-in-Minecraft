package bunger.group.client.tyler.god;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.tyler.entity.GodEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class GodEntityRenderer extends MobRenderer<GodEntity, GodEntityRenderState, GodEntityModel> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            MutuallyAssuredDestruction.MOD_ID, "textures/entity/god.png");

    public GodEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GodEntityModel(ctx.bakeLayer(ModEntityModelLayers.GOD)), 0.5f);
    }

    @Override
    public GodEntityRenderState createRenderState() {
        return new GodEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(GodEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(GodEntityRenderState state, PoseStack poseStack) {
        poseStack.scale(8.0f, 8.0f, 8.0f);
        poseStack.translate(0.0, 0.2, 0.0);
    }

    @Override
    public void extractRenderState(GodEntity entity, GodEntityRenderState state, float tickDelta) {
        super.extractRenderState(entity, state, tickDelta);
        state.ageScale = 4.0f;
    }
}