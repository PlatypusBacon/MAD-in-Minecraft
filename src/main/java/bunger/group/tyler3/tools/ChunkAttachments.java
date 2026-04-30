package bunger.group.tyler3.tools;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public class ChunkAttachments {

    public static final AttachmentType<ChunkAttachmentData> STICKY_FACES =
            AttachmentRegistry.<ChunkAttachmentData>builder()
                    .persistent(ChunkAttachmentData.CODEC)
                    .buildAndRegister(
                            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "sticky_faces")
                    );

    public static void initialize() {
        MutuallyAssuredDestruction.LOGGER.debug("Registering chunk attachment types");
    }
}