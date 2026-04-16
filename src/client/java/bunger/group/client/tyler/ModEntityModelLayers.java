package bunger.group.client.tyler;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.squirrel.SquirrelEntityModel;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
    public static final ModelLayerLocation SQUIRREL = createMain("squirrel");
    public static final ModelLayerLocation SQUIRREL_BEAR = createMain("squirrel_bear");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SQUIRREL, SquirrelEntityModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SQUIRREL_BEAR, SquirrelBearEntityModel::getTexturedModelData);

    }
}