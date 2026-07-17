package bunger.group.tyler.item;

import bunger.group.tyler.entity.SquirrelBearEntity;
import bunger.group.tyler.entity.SquirrelEntity;
import bunger.group.tyler.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SquirrelGunItem extends Item {

    private static final int RELOAD_TICKS = 60;
    private static final float SQUIRREL_DAMAGE = 50F;
    private static final float OTHER_DAMAGE = 4F;
    private static final double RANGE = 32.0;

    private static final String LOADED_KEY = "Loaded";

    public SquirrelGunItem(Properties properties) {
        super(properties);
    }

    private boolean isLoaded(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        return data.copyTag().getBoolean(LOADED_KEY).orElse(false);
    }

    private void setLoaded(ItemStack stack, boolean loaded) {
        CompoundTag tag = stack.has(DataComponents.CUSTOM_DATA)
                ? stack.get(DataComponents.CUSTOM_DATA).copyTag()
                : new CompoundTag();
        tag.putBoolean(LOADED_KEY, loaded);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        if (isLoaded(stack)) {
            shoot(world, player);
            setLoaded(stack, false);
            player.getCooldowns().addCooldown(stack, 1);
        } else {
            setLoaded(stack, true);
            player.getCooldowns().addCooldown(stack, RELOAD_TICKS);
            player.sendSystemMessage(Component.literal("Reloading..."));
            world.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.RELOAD,
                    SoundSource.PLAYERS,
                    1.0f,
                    0.9f + world.getRandom().nextFloat() * 0.2f
            );
        }

        return InteractionResult.SUCCESS;
    }

    private void shoot(Level world, Player player) {
        world.playSound(
                null,
                player.blockPosition(),
                ModSounds.GUN_FIRE,
                SoundSource.PLAYERS,
                1.0f,
                0.9f + world.getRandom().nextFloat() * 0.2f
        );

        var start = player.getEyePosition();
        var look = player.getLookAngle();
        var end = start.add(look.scale(RANGE));

        AABB searchBox = new AABB(start, end).inflate(1.0);
        List<LivingEntity> entities = world.getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                e -> e != player && e.isAlive()
        );

        LivingEntity target = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity entity : entities) {
            var hitBox = entity.getBoundingBox().inflate(0.3);
            var hit = hitBox.clip(start, end);

            if (hit.isPresent()) {
                double dist = start.distanceTo(hit.get());
                if (dist < closestDist) {
                    closestDist = dist;
                    target = entity;
                }
            }
        }

        if (target != null) {
            float damage = (target instanceof SquirrelEntity || target instanceof SquirrelBearEntity)
                    ? SQUIRREL_DAMAGE
                    : OTHER_DAMAGE;

            target.hurt(
                    world.damageSources().playerAttack(player),
                    damage
            );
        }
    }

    public static ItemStack createLoaded(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(LOADED_KEY, true);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }
}