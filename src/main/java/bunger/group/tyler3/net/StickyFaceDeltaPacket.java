package bunger.group.tyler3.net;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record StickyFaceDeltaPacket(long pos, byte mask) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<StickyFaceDeltaPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "sticky_face_delta")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, StickyFaceDeltaPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> { buf.writeLong(pkt.pos()); buf.writeByte(pkt.mask()); },
                    buf -> new StickyFaceDeltaPacket(buf.readLong(), buf.readByte())
            );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}