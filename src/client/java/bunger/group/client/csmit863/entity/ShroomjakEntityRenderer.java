package bunger.group.client.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.entity.ShroomjakEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class ShroomjakEntityRenderer extends MobRenderer<ShroomjakEntity, ShroomjakRenderState, ShroomjakEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/shroomjak.png");

    public ShroomjakEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ShroomjakEntityModel(context.bakeLayer(ModEntityModelLayers.SHROOMJAK)), 0.375f); // 0.375 shadow radius
    }

    @Override
    public ShroomjakRenderState createRenderState() {
        return new ShroomjakRenderState();
    }

    @Override
    public Identifier getTextureLocation(ShroomjakRenderState state) {
        return TEXTURE;
    }

    @Override
    public void extractRenderState(ShroomjakEntity entity, ShroomjakRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.scale = 0.9f;
    }

}