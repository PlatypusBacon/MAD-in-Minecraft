// StickyGooModel.java
package bunger.group.client.tyler3.goop;

import bunger.group.tyler3.tools.ClientAttachmentCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class StickyGooModel {

    public static final Identifier MODEL_ID = Identifier.fromNamespaceAndPath(
            "mutually-assured-destruction", "block/sticky_goo"
    );

    public static final ExtraModelKey<BlockStateModel> MODEL_KEY = ExtraModelKey.create();

    public static void register() {
        ModelLoadingPlugin.register(ctx ->
                ctx.addModel(MODEL_KEY, SimpleUnbakedExtraModel.blockStateModel(MODEL_ID))
        );
    }

    public static BlockStateModel getModel() {
        return Minecraft.getInstance().getModelManager().getModel(MODEL_KEY);
    }

    public static boolean isModelMissing() {
        return getModel() == null;
    }

    public static Set<Direction> getFacesAt(BlockPos pos) {
        return ClientAttachmentCache.getFaces(pos);
    }
}