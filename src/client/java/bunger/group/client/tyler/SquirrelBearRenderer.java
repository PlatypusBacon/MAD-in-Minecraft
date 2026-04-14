package bunger.group.client.tyler;

import bunger.group.client.ModModelLayers;
import bunger.group.client.tyler.SquirrelBearModel;
import bunger.group.entity.SquirrelBearEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SquirrelBearRenderer extends MobRenderer<SquirrelBearEntity, SquirrelBearModel<SquirrelBearEntity>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("mutually-assured-destruction",
                    "textures/entity/squirrel_bear.png");

    public SquirrelBearRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SquirrelBearModel<>(ctx.bakeLayer(ModModelLayers.SQUIRREL_BEAR)), 0.5f);
    }

    @Override
    protected void scale(SquirrelBearEntity entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(4.0f, 4.0f, 4.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SquirrelBearEntity entity) {
        return TEXTURE;
    }
}