package bunger.group.client.tyler;


import bunger.group.client.ModModelLayers;
import bunger.group.entity.SquirrelEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SquirrelRenderer extends MobRenderer<SquirrelEntity, SquirrelModel<SquirrelEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("mutually-assured-destruction", "textures/entity/squirrel.png");

    public SquirrelRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SquirrelModel<>(ctx.bakeLayer(ModModelLayers.SQUIRREL)), 0.9f); // increased shadow radius
    }
    @Override
    protected void scale(SquirrelEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(2.5f, 2.5f, 2.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SquirrelEntity entity) {
        return TEXTURE;
    }
}