package bunger.group.client.alex.entity.model;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.ModEntityTypes;
import bunger.group.client.alex.entity.renderer.GoblinGruntEntityRenderer;
import bunger.group.client.alex.entity.renderer.WraithEntityRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class ModEntityModelLayers {

    public static final ModelLayerLocation WRAITH = createMain("wraith");
    public static final ModelLayerLocation GOBLIN_GRUNT = createMain("goblin");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.WRAITH, WraithEntityModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GOBLIN_GRUNT, GoblinGruntEntityModel::createBodyLayer);
    }

    public static void registerRenderers() {
        EntityRenderers.register(ModEntityTypes.WRAITH, WraithEntityRenderer::new);
        EntityRenderers.register(ModEntityTypes.GOBLIN_GRUNT, GoblinGruntEntityRenderer::new);
    }
}