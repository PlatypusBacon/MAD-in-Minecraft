package bunger.group.tyler2.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JerkyItem extends Item {

    public JerkyItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal(player.getPlainTextName() + " is jerking it."));
        }
        return super.finishUsingItem(itemStack, level, entity);
    }
}
