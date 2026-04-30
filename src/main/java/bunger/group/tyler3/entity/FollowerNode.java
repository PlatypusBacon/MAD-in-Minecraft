package bunger.group.tyler3.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents one block in the follower tree attached to a platform entity.
 * offset is relative to the platform's block position when movement started.
 */
public class FollowerNode {

    public final Vec3i offset;
    public BlockState state;
    // Which faces of this block have goo pointing to other followers or to the platform
    public final Set<Direction> gooFaces;

    public FollowerNode(Vec3i offset, BlockState state, Set<Direction> gooFaces) {
        this.offset = offset;
        this.state = state;
        this.gooFaces = EnumSet.copyOf(gooFaces.isEmpty() ? EnumSet.noneOf(Direction.class) : gooFaces);
    }
}