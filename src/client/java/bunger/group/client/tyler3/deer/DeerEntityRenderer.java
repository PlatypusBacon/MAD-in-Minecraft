package bunger.group.client.tyler3.deer;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.tyler3.entity.DeerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class DeerEntityRenderer extends MobRenderer<DeerEntity, DeerEntityRenderState, DeerEntityModel>
{
    private final static float BASE_SHADOW_RADIUS = 0.75f;
    private final static float BABY_MULTIPLIER = 0.6f;

    public DeerEntityRenderer(EntityRendererProvider.Context context)
    {
        super(context, new DeerEntityModel(context.bakeLayer(ModEntityModelLayers.DEER)), BASE_SHADOW_RADIUS);
    }

    @Override
    public @NotNull Identifier getTextureLocation(DeerEntityRenderState state)
    {
        return Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public @NotNull DeerEntityRenderState createRenderState()
    {
        return new DeerEntityRenderState();
    }

    @Override
    public void extractRenderState(DeerEntity deerEntity, DeerEntityRenderState deerEntityRenderState, float delta)
    {
        super.extractRenderState(deerEntity, deerEntityRenderState, delta);
        deerEntityRenderState.eatGrassAnimationState.copyFrom(deerEntity.eatGrassAnimationState);
    }

}