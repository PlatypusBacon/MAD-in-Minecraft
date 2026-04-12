package bunger.group.client.ethan;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.ethan.ProphetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class ProphetEntityRenderer extends MobRenderer<ProphetEntity, ProphetEntityRenderState, ProphetEntityModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/prophet.png");

	public ProphetEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new ProphetEntityModel(context.bakeLayer(ModEntityModelLayers.PROPHET)), 0.375f); // 0.375 shadow radius
		    this.addLayer(new EyesLayer<>(this) {
        	@Override
				public RenderType renderType() {
					return RenderTypes.eyes(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/prophet_glow.png"));
        		}
    });
	}

	@Override
	public ProphetEntityRenderState createRenderState() {
		return new ProphetEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(ProphetEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void extractRenderState(ProphetEntity entity, ProphetEntityRenderState state, float tickDelta) {
    	super.extractRenderState(entity, state, tickDelta);
    	state.xRot = entity.getXRot();
}
}
