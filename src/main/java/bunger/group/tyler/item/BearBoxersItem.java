package bunger.group.tyler.item;

import bunger.group.tyler.combat.ParryHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BearBoxersItem extends Item {

    public static final Map<UUID, Boolean> PUNCH_SIDE = new HashMap<>();
    public static final Map<UUID, Boolean> PUNCH_SIDE_CLIENT = new HashMap<>();
    public BearBoxersItem(Properties properties) {
        super(properties);
    }

    public static void onPunch(Player player) {
        boolean useOffhand = PUNCH_SIDE.getOrDefault(player.getUUID(), false);
        PUNCH_SIDE.put(player.getUUID(), !useOffhand);
        // true = force swing even if animation already playing
        player.swing(useOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, true);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        player.startUsingItem(hand);
        if (!level.isClientSide()) {
            ParryHandler.beginParry(player);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 15;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }
}