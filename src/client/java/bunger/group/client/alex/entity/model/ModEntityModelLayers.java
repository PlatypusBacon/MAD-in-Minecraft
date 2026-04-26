package bunger.group.client.alex.entity.model;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class ModEntityModelLayers {

    public static final ModelLayerLocation WRAITH = createMain("wraith");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.WRAITH, WraithEntityModel::createBodyLayer);
    }
}