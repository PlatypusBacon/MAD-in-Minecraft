package bunger.group.tyler3.network;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sent server → client when a player picks up an item that unlocks a tome recipe page.
 */
public record UnlockRecipePagePayload(String recipeId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<UnlockRecipePagePayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "unlock_recipe_page")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockRecipePagePayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeUtf(payload.recipeId()),
                    buf -> new UnlockRecipePagePayload(buf.readUtf())
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}