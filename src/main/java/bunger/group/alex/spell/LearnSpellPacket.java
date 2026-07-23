package bunger.group.alex.spell;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record LearnSpellPacket(int spellIndex) implements CustomPacketPayload {

    public static final Type<LearnSpellPacket> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "learn_spell"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LearnSpellPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, LearnSpellPacket::spellIndex,
                    LearnSpellPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}