// src/client/java/com/yourname/yourmod/ModModelLayers.java
package bunger.group.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation SQUIRREL = new ModelLayerLocation(
            new ResourceLocation("yourmod", "squirrel"), "main"
    );

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(
                SQUIRREL, SquirrelModel::getTexturedModelData
        );
    }
}