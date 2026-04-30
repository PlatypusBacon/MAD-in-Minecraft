package bunger.group.tyler3.tools;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkAttachmentHelper {

    private static final Set<Long> PENDING_LINKS = ConcurrentHashMap.newKeySet();

    private static final long[] SALTS = {
            0x9e3779b97f4a7c15L, 0x6c62272e07bb0142L,
            0x94d049bb133111ebL, 0xbf58476d1ce4e5b9L,
            0x3f1832b3c4df0e31L, 0x8a5cd789635d2458L
    };

    private static long encodePending(BlockPos pos, Direction face) {
        return pos.asLong() ^ SALTS[face.ordinal()];
    }

    public static void markPending(BlockPos pos, Direction face) {
        PENDING_LINKS.add(encodePending(pos, face));
    }

    public static boolean isPending(BlockPos pos, Direction face) {
        return PENDING_LINKS.contains(encodePending(pos, face));
    }

    public static void clearPending(BlockPos pos, Direction face) {
        PENDING_LINKS.remove(encodePending(pos, face));
    }

    public static boolean hasAttachment(Level level, BlockPos pos, Direction face) {
        ChunkAttachmentData data = getChunkData(level, pos);
        return data != null && data.hasFace(pos, face);
    }

    public static Set<Direction> getStickyFaces(Level level, BlockPos pos) {
        ChunkAttachmentData data = getChunkData(level, pos);
        if (data == null) return Collections.emptySet();
        return data.getStickyFaces(pos);
    }

    public static void applyAttachmentOneFace(Level level, BlockPos pos, Direction face) {
        setFace(level, pos, face, true);
        markChunkDirty(level, pos);
    }

    public static void removeAttachmentOneFace(Level level, BlockPos pos, Direction face) {
        setFace(level, pos, face, false);
        markChunkDirty(level, pos);
    }

    public static void clearAllFaces(Level level, BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return;
        ChunkAttachmentData data = chunk.getAttached(ChunkAttachments.STICKY_FACES);
        if (data != null) {
            data.clearPos(pos);
            if (data.isEmpty()) chunk.removeAttached(ChunkAttachments.STICKY_FACES);
        }
        markChunkDirty(level, pos);
    }

    public static ChunkAttachmentData getChunkData(Level level, BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return null;
        return chunk.getAttached(ChunkAttachments.STICKY_FACES);
    }

    private static void setFace(Level level, BlockPos pos, Direction face, boolean value) {
        ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return;
        ChunkAttachmentData data = chunk.getAttached(ChunkAttachments.STICKY_FACES);
        if (data == null) {
            if (!value) return;
            data = new ChunkAttachmentData();
            chunk.setAttached(ChunkAttachments.STICKY_FACES, data);
        }
        data.setFace(pos, face, value);
        if (data.isEmpty()) chunk.removeAttached(ChunkAttachments.STICKY_FACES);
    }

    private static void markChunkDirty(Level level, BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk != null) chunk.markUnsaved();
    }
}