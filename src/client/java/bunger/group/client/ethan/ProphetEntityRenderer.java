package bunger.group.client.ethan;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.ethan.ProphetEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class ProphetEntityRenderer extends MobRenderer<ProphetEntity, ProphetEntityRenderState, ProphetEntityModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/prophet.png");

	public ProphetEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new ProphetEntityModel(context.bakeLayer(ModEntityModelLayers.PROPHET)), 0.375f); // 0.375 shadow radius
	}

	@Override
	public ProphetEntityRenderState createRenderState() {
		return new ProphetEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(ProphetEntityRenderState state) {
		return TEXTURE;
	}
}
