package bunger.group.block;

import bunger.group.event.God;
import bunger.group.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Block;
import java.util.List;

public class SquirrelWife extends Block {
    public SquirrelWife(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, net.minecraft.server.level.ServerLevel level,
                     BlockPos pos, net.minecraft.util.RandomSource random) {
        // schedule next tick
        level.scheduleTick(pos, this, 20); // check every second

        // find armor stands directly above this block
        List<ArmorStand> stands = level.getEntitiesOfClass(
                ArmorStand.class,
                new AABB(pos).inflate(0.5, 1.5, 0.5)
        );

        if (stands.isEmpty()) return;
        ArmorStand stand = stands.get(0);

        boolean headFilled  = stand.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SQUEATHER_HEAD);
        boolean chestFilled = stand.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.SQUEATHER_CHEST);
        boolean legsFilled  = stand.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.SQUEATHER_LEGS);
        boolean feetFilled  = stand.getItemBySlot(EquipmentSlot.FEET).is(ModItems.SQUEATHER_FEET);

        if (headFilled && chestFilled && legsFilled && feetFilled) {
            God.start(level, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 20);
        }
    }
}
