package bunger.group.alex;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ManaPacket(int current, int max) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManaPacket> TYPE = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_sync")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaPacket> CODEC = StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.INT, ManaPacket::current,
            net.minecraft.network.codec.ByteBufCodecs.INT, ManaPacket::max,
            ManaPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}