// src/client/java/com/yourname/yourmod/ModModelLayers.java
package bunger.group.client;

import bunger.group.client.tyler.SquirrelBearModel;
import bunger.group.client.tyler.SquirrelModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation SQUIRREL = new ModelLayerLocation(
            new ResourceLocation("mutually-assured-destruction", "squirrel"), "main"
    );
    public static final ModelLayerLocation SQUIRREL_BEAR =
            new ModelLayerLocation(
                    new ResourceLocation("mutually-assured-destruction", "squirrel_bear"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(
                SQUIRREL, SquirrelModel::getTexturedModelData
        );
        EntityModelLayerRegistry.registerModelLayer(
                SQUIRREL_BEAR, SquirrelBearModel::getTexturedModelData
        );
    }
}