package bunger.group.client.tyler.squirrel_wife;

import bunger.group.tyler.entity.SquirrelWifeEntity;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.ArmorStand;

public class SquirrelWifeEntityRenderer extends ArmorStandRenderer {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath("mutually-assured-destruction",
                    "textures/entity/squirrel_wife.png");

    public SquirrelWifeEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTextureLocation(ArmorStandRenderState state) {
        return TEXTURE;
    }
}