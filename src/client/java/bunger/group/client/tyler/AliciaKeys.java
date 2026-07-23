package bunger.group.client.tyler;

import bunger.group.MutuallyAssuredDestruction;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;

public class AliciaKeys {
    public static final KeyMapping RELOAD_KEY = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.mutually-assured-destruction.reload",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_R,
                    KeyMapping.Category.GAMEPLAY
            )
    );
    public static void initialize() {
        MutuallyAssuredDestruction.LOGGER.info("Registering " + MutuallyAssuredDestruction.MOD_ID + " Keys");

    }
}
