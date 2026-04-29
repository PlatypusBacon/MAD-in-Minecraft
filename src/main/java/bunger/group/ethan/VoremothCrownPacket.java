package bunger.group.ethan;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import bunger.group.MutuallyAssuredDestruction;

public record VoremothCrownPacket(int targetEntityId, int laserTimer) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<VoremothCrownPacket> TYPE =
        new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "voremoth_crown_laser")
        );

    public static final StreamCodec<FriendlyByteBuf, VoremothCrownPacket> CODEC =
        StreamCodec.composite(
            net.minecraft.network.codec.ByteBufCodecs.INT, VoremothCrownPacket::targetEntityId,
            net.minecraft.network.codec.ByteBufCodecs.INT, VoremothCrownPacket::laserTimer,
            VoremothCrownPacket::new
        );

    @Override
    public CustomPacketPayload.Type<VoremothCrownPacket> type() {
        return TYPE;
    }

    public static void registerPacket() {
        PayloadTypeRegistry.clientboundPlay().register(TYPE, CODEC);
    }
}