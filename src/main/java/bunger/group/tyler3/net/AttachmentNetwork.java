package bunger.group.tyler3.net;

import bunger.group.tyler3.tools.ChunkAttachmentData;
import bunger.group.tyler3.tools.ChunkAttachments;
import bunger.group.tyler3.tools.ClientAttachmentCache;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Map;

public class AttachmentNetwork {
    public static void registerServer() {
        PayloadTypeRegistry.clientboundPlay().register(
                StickyFacesChunkPacket.TYPE, StickyFacesChunkPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(
                StickyFaceDeltaPacket.TYPE, StickyFaceDeltaPacket.CODEC);

        // Send chunk data when a player receives a chunk
        // Hook this into your chunk watch event
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                StickyFacesChunkPacket.TYPE,
                (pkt, ctx) -> pkt.faces().forEach((k, v) ->
                        ClientAttachmentCache.setFaces(BlockPos.of(k), v))
        );
        ClientPlayNetworking.registerGlobalReceiver(
                StickyFaceDeltaPacket.TYPE,
                (pkt, ctx) -> ClientAttachmentCache.setFaces(BlockPos.of(pkt.pos()), pkt.mask())
        );
    }

    // Call this when a player starts watching a chunk
    public static void sendChunkToPlayer(ServerPlayer player, ChunkAccess chunk) {
        ChunkAttachmentData data = chunk.getAttached(ChunkAttachments.STICKY_FACES);
        if (data == null) return;
        // Access posToFaces via a new getter you add to ChunkAttachmentData
        Map<Long, Byte> faces = data.getAllFaces();
        if (faces.isEmpty()) return;
        ServerPlayNetworking.send(player, new StickyFacesChunkPacket(faces));
    }

    // Call this after applyAttachmentOneFace / removeAttachmentOneFace
    public static void sendDeltaToNearbyPlayers(
            net.minecraft.server.level.ServerLevel level,
            BlockPos pos, byte newMask) {
        StickyFaceDeltaPacket pkt = new StickyFaceDeltaPacket(pos.asLong(), newMask);
        level.getServer().getPlayerList().getPlayers().stream()
                .filter(p -> p.level() == level &&
                        p.blockPosition().distSqr(pos) < 128 * 128)
                .forEach(p -> ServerPlayNetworking.send(p, pkt));
    }
}