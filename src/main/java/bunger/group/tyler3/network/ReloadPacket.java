package bunger.group.tyler3.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import bunger.group.MutuallyAssuredDestruction;

public record ReloadPacket() implements CustomPacketPayload {
    public static final Type<ReloadPacket> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "reload")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ReloadPacket> CODEC =
            StreamCodec.unit(new ReloadPacket());

    public Type<ReloadPacket> type() {
        return TYPE;
    }
}