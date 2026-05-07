package bunger.group.client.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.entity.OverseerEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

public class OverseerEntityRenderer extends EntityRenderer<OverseerEntity, EntityRenderState> {

    public OverseerEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

}