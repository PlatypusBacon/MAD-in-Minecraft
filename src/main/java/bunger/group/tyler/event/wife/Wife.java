package bunger.group.tyler.event.wife;

import bunger.group.tyler.entity.SquirrelWifeEntity;
import bunger.group.tyler.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class Wife {
    public static void start(ServerLevel level, SquirrelWifeEntity wife) {

        net.minecraft.world.item.Item drop = ModItems.SQUIRREL_STAPELER;
        ItemStack stack = new ItemStack(drop);
        wife.spawnAtLocation(level, stack);
        System.out.println("Wife Time baby");
        wife.kill(level);
    }
}
