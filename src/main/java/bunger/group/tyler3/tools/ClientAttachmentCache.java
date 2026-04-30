package bunger.group.tyler3.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientAttachmentCache {

    private static final Map<Long, Byte> cache = new ConcurrentHashMap<>();

    public static void setFaces(BlockPos pos, byte mask) {
        if (mask == 0) cache.remove(pos.asLong());
        else cache.put(pos.asLong(), mask);
    }

    public static void clearChunk(int chunkX, int chunkZ) {
        // Clear all entries that fall within this chunk
        cache.keySet().removeIf(key -> {
            BlockPos pos = BlockPos.of(key);
            return (pos.getX() >> 4) == chunkX && (pos.getZ() >> 4) == chunkZ;
        });
    }

    public static Set<Direction> getFaces(BlockPos pos) {
        byte mask = cache.getOrDefault(pos.asLong(), (byte) 0);
        Set<Direction> result = EnumSet.noneOf(Direction.class);
        for (Direction d : Direction.values()) {
            if ((mask & (1 << d.ordinal())) != 0) result.add(d);
        }
        return result;
    }

    public static boolean hasFace(BlockPos pos, Direction face) {
        byte mask = cache.getOrDefault(pos.asLong(), (byte) 0);
        return (mask & (1 << face.ordinal())) != 0;
    }
}