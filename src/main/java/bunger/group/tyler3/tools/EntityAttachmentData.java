package bunger.group.tyler3.tools;

import bunger.group.tyler3.entity.FollowerNode;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class EntityAttachmentData {

    private final Set<Direction> platformFaces = EnumSet.noneOf(Direction.class);
    private final Map<Vec3i, FollowerNode> followers = new LinkedHashMap<>();

    public void addPlatformFace(Direction face) { platformFaces.add(face); }
    public void removePlatformFace(Direction face) { platformFaces.remove(face); }
    public boolean hasPlatformFace(Direction face) { return platformFaces.contains(face); }
    public Set<Direction> getPlatformFaces() { return Collections.unmodifiableSet(platformFaces); }

    public void addFollower(Vec3i offset, BlockState state, Set<Direction> gooFaces) {
        followers.put(offset, new FollowerNode(offset, state, gooFaces));
    }
    public void removeFollower(Vec3i offset) { followers.remove(offset); }
    public boolean hasFollower(Vec3i offset) { return followers.containsKey(offset); }
    public FollowerNode getFollower(Vec3i offset) { return followers.get(offset); }
    public Map<Vec3i, FollowerNode> getFollowers() { return Collections.unmodifiableMap(followers); }
    public boolean isEmpty() { return platformFaces.isEmpty() && followers.isEmpty(); }
    public void clear() { platformFaces.clear(); followers.clear(); }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        byte faceMask = 0;
        for (Direction face : platformFaces) {
            faceMask |= (byte)(1 << face.ordinal());
        }
        tag.putByte("PlatformFaces", faceMask);

        ListTag followerList = new ListTag();
        for (Map.Entry<Vec3i, FollowerNode> entry : followers.entrySet()) {
            CompoundTag followerTag = new CompoundTag();
            Vec3i offset = entry.getKey();
            FollowerNode node = entry.getValue();

            followerTag.putInt("OX", offset.getX());
            followerTag.putInt("OY", offset.getY());
            followerTag.putInt("OZ", offset.getZ());

            Identifier blockId = BuiltInRegistries.BLOCK.getKey(node.state.getBlock());
            followerTag.putString("Block", blockId.toString());

            byte gooMask = 0;
            for (Direction face : node.gooFaces) {
                gooMask |= (byte)(1 << face.ordinal());
            }
            followerTag.putByte("GooFaces", gooMask);

            followerList.add(followerTag);
        }
        tag.put("Followers", followerList);

        return tag;
    }

    public static EntityAttachmentData load(CompoundTag tag) {
        EntityAttachmentData data = new EntityAttachmentData();

        byte faceMask = tag.getByteOr("PlatformFaces", (byte)0);
        for (Direction face : Direction.values()) {
            if ((faceMask & (1 << face.ordinal())) != 0) {
                data.platformFaces.add(face);
            }
        }

        ListTag followerList = tag.getListOrEmpty("Followers");
        for (int i = 0; i < followerList.size(); i++) {
            CompoundTag followerTag = followerList.getCompoundOrEmpty(i);

            int ox = followerTag.getIntOr("OX", 0);
            int oy = followerTag.getIntOr("OY", 0);
            int oz = followerTag.getIntOr("OZ", 0);
            Vec3i offset = new Vec3i(ox, oy, oz);

            String blockId = followerTag.getStringOr("Block", "minecraft:air");
            Block block = BuiltInRegistries.BLOCK
                    .get(Identifier.parse(blockId))
                    .map(Holder::value)
                    .orElse(Blocks.AIR);
            BlockState state = block.defaultBlockState();

            byte gooMask = followerTag.getByteOr("GooFaces", (byte)0);
            Set<Direction> gooFaces = EnumSet.noneOf(Direction.class);
            for (Direction face : Direction.values()) {
                if ((gooMask & (1 << face.ordinal())) != 0) {
                    gooFaces.add(face);
                }
            }

            data.followers.put(offset, new FollowerNode(offset, state, gooFaces));
        }

        return data;
    }
}