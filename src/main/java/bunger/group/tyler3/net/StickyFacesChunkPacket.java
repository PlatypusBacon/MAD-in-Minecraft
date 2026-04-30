package bunger.group.tyler3.net;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.tools.ClientAttachmentCache;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

// Sent when a chunk loads or bulk update needed
public record StickyFacesChunkPacket(Map<Long, Byte> faces) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<StickyFacesChunkPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "sticky_faces_chunk")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, StickyFacesChunkPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeInt(pkt.faces().size());
                        pkt.faces().forEach((k, v) -> { buf.writeLong(k); buf.writeByte(v); });
                    },
                    buf -> {
                        int size = buf.readInt();
                        Map<Long, Byte> map = new HashMap<>(size);
                        for (int i = 0; i < size; i++) map.put(buf.readLong(), buf.readByte());
                        return new StickyFacesChunkPacket(map);
                    }
            );

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}