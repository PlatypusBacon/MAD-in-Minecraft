package bunger.group.csmit863;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record MadnessPacket(int current, int max) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MadnessPacket> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "madness"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MadnessPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, MadnessPacket::current,
                    ByteBufCodecs.VAR_INT, MadnessPacket::max,
                    MadnessPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}