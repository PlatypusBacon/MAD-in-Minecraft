package bunger.group.block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
public class SummoningCircle extends Block {

    // very thin — just 1px tall, no collision
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

    public SummoningCircle(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world,
                               BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world,
                                        BlockPos pos, CollisionContext context) {
        return Shapes.empty(); // no hitbox — players walk through/over it
    }
}


