package bunger.group.tyler.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record PunchSidePacket(UUID playerUUID, boolean useOffhand) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PunchSidePacket> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath("mutually-assured-destruction", "punch_side")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PunchSidePacket> CODEC =
            StreamCodec.composite(
                    StreamCodec.of(
                            (buf, uuid) -> {
                                buf.writeLong(uuid.getMostSignificantBits());
                                buf.writeLong(uuid.getLeastSignificantBits());
                            },
                            buf -> new UUID(buf.readLong(), buf.readLong())
                    ),
                    PunchSidePacket::playerUUID,
                    StreamCodec.of(
                            (buf, val) -> buf.writeBoolean(val),
                            buf -> buf.readBoolean()
                    ),
                    PunchSidePacket::useOffhand,
                    PunchSidePacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Only call from server-side code
    public static void registerServer() {
        PayloadTypeRegistry.clientboundPlay().register(TYPE, CODEC);
    }

    public static void send(ServerPlayer player, boolean useOffhand) {
        ServerPlayNetworking.send(player, new PunchSidePacket(player.getUUID(), useOffhand));
    }
}