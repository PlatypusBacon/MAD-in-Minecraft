package bunger.group.tyler.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

public class SquirrelStapelerItem extends Item {
    public SquirrelStapelerItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(final ItemStack stack) {
        return ItemUseAnimation.BRUSH;
    }

    @Override
    public int getUseDuration(final ItemStack stack, final LivingEntity entity) {
        return 20;
    }

    @Override
    public ItemStack finishUsingItem(final ItemStack stack, final Level level, final LivingEntity user) {
        if (level instanceof ServerLevel serverLevel) {
            user.hurtServer(serverLevel, level.damageSources().sweetBerryBush(), 2.0f);
        }
        return stack;
    }
}
