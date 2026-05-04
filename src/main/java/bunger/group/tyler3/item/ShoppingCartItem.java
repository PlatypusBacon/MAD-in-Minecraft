package bunger.group.tyler3.item;

import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler3.entity.ShoppingCartEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ShoppingCartItem extends Item {
    public ShoppingCartItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ServerLevel serverLevel = (ServerLevel) level;
        ItemStack stack = player.getItemInHand(hand);

        Vec3 look = player.getLookAngle();
        Vec3 flatLook = new Vec3(look.x, 0, look.z).normalize();
        Vec3 spawnPos = player.position().add(flatLook.scale(1.5));

        ShoppingCartEntity cart = new ShoppingCartEntity(
                ModEntities.SHOPPING_CART, serverLevel);
        cart.snapTo(spawnPos.x, spawnPos.y, spawnPos.z,
                player.getYRot(), 0);
        serverLevel.addFreshEntity(cart);

        // Consume one item from the stack
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}