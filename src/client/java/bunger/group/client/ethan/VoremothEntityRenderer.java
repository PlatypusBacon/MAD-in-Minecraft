package bunger.group.client.ethan;

import com.mojang.blaze3d.vertex.PoseStack;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.ethan.VoremothEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class VoremothEntityRenderer extends MobRenderer<VoremothEntity, VoremothEntityRenderState, VoremothEntityModel> {
	private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/voremoth.png");

	public VoremothEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new VoremothEntityModel(context.bakeLayer(ModEntityModelLayers.VOREMOTH)), 30);
		    this.addLayer(new EyesLayer<>(this) {
        	@Override
				public RenderType renderType() {
					return RenderTypes.eyes(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/voremoth_glow.png"));
        		}
    });
	}

	@Override
	public VoremothEntityRenderState createRenderState() {
		return new VoremothEntityRenderState();
	}

	@Override
	public Identifier getTextureLocation(VoremothEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void extractRenderState(VoremothEntity entity, VoremothEntityRenderState state, float tickDelta) {
    	super.extractRenderState(entity, state, tickDelta);
    	state.xRot = entity.getXRot();
		state.ageInTicks = entity.tickCount + tickDelta;
	}

	// @Override
    // public void scale(VoremothEntityRenderState state, PoseStack poseStack, float partialTick) {
    //     poseStack.scale(5.0F, 5.0F, 5.0F);
    //     super.scale(state, poseStack, partialTick);
    // }
}
