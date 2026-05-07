package bunger.group.client.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
    public static final ModelLayerLocation SHROOMJAK = createMain("shroomjak");
    public static final ModelLayerLocation OVERSEER = createMain("overseer");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SHROOMJAK, ShroomjakEntityModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.OVERSEER, OverseerEntityModel::getTexturedModelData);
    }
}