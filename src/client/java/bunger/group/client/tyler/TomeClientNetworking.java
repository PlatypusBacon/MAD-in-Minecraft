package bunger.group.client.tyler;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler3.gui.PaintOverlay;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Client-side network handlers. Register via {@link #register()} from your
 * client entrypoint.
 */
public final class TomeClientNetworking {

    private TomeClientNetworking() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                UnlockRecipePagePayload.TYPE,
                (payload, context) -> {
                    MutuallyAssuredDestruction.LOGGER.info("[Tome] Received unlock packet for: {}", payload.recipeId());
                    boolean added = PaintOverlay.unlockRecipe(payload.recipeId());
                    MutuallyAssuredDestruction.LOGGER.info("[Tome] unlockRecipe returned: {}", added);
                }
        );
    }
}