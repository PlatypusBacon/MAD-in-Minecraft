package bunger.group.tyler3.tools;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.*;

public class ChunkAttachmentData {

    private final Map<Long, Byte> posToFaces;

    public ChunkAttachmentData() {
        this.posToFaces = new HashMap<>();
    }

    private ChunkAttachmentData(Map<Long, Byte> posToFaces) {
        this.posToFaces = new HashMap<>(posToFaces);
    }
    public Map<Long, Byte> getAllFaces() {
        return Collections.unmodifiableMap(posToFaces);
    }
    public void setFace(BlockPos pos, Direction face, boolean sticky) {
        long key = pos.asLong();
        byte current = posToFaces.getOrDefault(key, (byte) 0);
        if (sticky) {
            current |= (byte)(1 << face.ordinal());
        } else {
            current &= (byte)~(1 << face.ordinal());
        }
        if (current == 0) posToFaces.remove(key);
        else posToFaces.put(key, current);
    }

    public boolean hasFace(BlockPos pos, Direction face) {
        byte mask = posToFaces.getOrDefault(pos.asLong(), (byte) 0);
        return (mask & (1 << face.ordinal())) != 0;
    }

    public Set<Direction> getStickyFaces(BlockPos pos) {
        byte mask = posToFaces.getOrDefault(pos.asLong(), (byte) 0);
        Set<Direction> result = EnumSet.noneOf(Direction.class);
        for (Direction d : Direction.values()) {
            if ((mask & (1 << d.ordinal())) != 0) result.add(d);
        }
        return result;
    }

    public void clearPos(BlockPos pos) {
        posToFaces.remove(pos.asLong());
    }

    public boolean isEmpty() {
        return posToFaces.isEmpty();
    }

    public static final Codec<ChunkAttachmentData> CODEC =
            Codec.unboundedMap(
                    Codec.STRING.xmap(Long::parseLong, Object::toString),
                    Codec.BYTE
            ).xmap(
                    map -> new ChunkAttachmentData(new HashMap<>(map)),
                    d -> d.posToFaces
            );
}