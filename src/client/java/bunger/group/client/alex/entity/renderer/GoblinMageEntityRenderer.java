package bunger.group.client.alex.entity.renderer;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.GoblinMageEntity;
import bunger.group.client.alex.entity.model.GoblinMageEntityModel;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.state.GoblinMageEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;

public class GoblinMageEntityRenderer extends MobRenderer<GoblinMageEntity, GoblinMageEntityRenderState, GoblinMageEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/goblin_mage.png");

    private final EntityRendererProvider.Context context;

    public GoblinMageEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GoblinMageEntityModel(context.bakeLayer(ModEntityModelLayers.GOBLIN_MAGE)), 0.3f);
        this.context = context;
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public GoblinMageEntityRenderState createRenderState() {
        return new GoblinMageEntityRenderState();
    }

    @Override
    public void extractRenderState(GoblinMageEntity entity, GoblinMageEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.walkAnimPos = entity.walkAnimation.position(tickProgress);
        state.walkAnimSpeed = entity.walkAnimation.speed(tickProgress);
        state.attackTime = entity.getAttackAnim(tickProgress);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, context.getItemModelResolver(), tickProgress);
    }

    @Override
    public Identifier getTextureLocation(GoblinMageEntityRenderState state) {
        return TEXTURE;
    }
}