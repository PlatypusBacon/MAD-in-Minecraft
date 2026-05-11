package bunger.group.bryan;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;
public class ChestTracker {

    // dimension → set of chest positions
    private static final Map<ResourceKey<Level>, Set<BlockPos>> CHESTS = new HashMap<>();

    public static void add(ResourceKey<Level> dim, BlockPos pos) {
        CHESTS.computeIfAbsent(dim, k -> new HashSet<>())
              .add(pos.immutable());
    }

    public static void remove(ResourceKey<Level> dim, BlockPos pos) {
        Set<BlockPos> set = CHESTS.get(dim);
        if (set != null) {
            set.remove(pos);
        }
    }

    public static Set<BlockPos> get(ResourceKey<Level> dim) {
        return CHESTS.getOrDefault(dim, Collections.emptySet());
    }

    public static Map<ResourceKey<Level>, Set<BlockPos>> getChests() {
        return CHESTS;
    }


}