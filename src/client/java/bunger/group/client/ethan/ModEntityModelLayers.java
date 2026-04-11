package bunger.group.client.ethan;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
	public static final ModelLayerLocation PROPHET = createMain("prophet");

	private static ModelLayerLocation createMain(String name) {
		return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
	}

	public static void registerModelLayers() {
		ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.PROPHET, ProphetEntityModel::getTexturedModelData);
	}
}
