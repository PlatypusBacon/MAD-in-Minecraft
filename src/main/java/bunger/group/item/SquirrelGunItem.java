package bunger.group.item;

import bunger.group.entity.SquirrelEntity;
import bunger.group.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SquirrelGunItem extends Item {

    private static final int RELOAD_TICKS = 60;        // 2 seconds
    private static final float SQUIRREL_DAMAGE = 1000F; // one hit kill
    private static final float OTHER_DAMAGE = 4F;     // half a heart
    private static final double RANGE = 32.0;           // block range

    // NBT key to track loaded state
    private static final String LOADED_KEY = "Loaded";

    public SquirrelGunItem(Properties settings) {
        super(settings);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        boolean loaded = stack.getOrCreateTag().getBoolean(LOADED_KEY);

        if (loaded) {
            // fire the gun
            shoot(world, player);
            stack.getOrCreateTag().putBoolean(LOADED_KEY, false);
            player.getCooldowns().addCooldown(this, 1); // tiny cooldown to prevent instant reload click
        } else {
            // begin reload — 2 second cooldown, then it's ready
            stack.getOrCreateTag().putBoolean(LOADED_KEY, true);
            player.getCooldowns().addCooldown(this, RELOAD_TICKS);
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Reloading..."), true
            );
        }

        return InteractionResultHolder.success(stack);
    }

    private void shoot(Level world, Player player) {
        // cast a ray from the player's eye position in the direction they're looking
        world.playSound(
                null,                          // null = play for all nearby players
                player.blockPosition(),
                ModSounds.GUN_FIRE,
                SoundSource.PLAYERS,
                1.0f,                          // volume
                0.9f + world.random.nextFloat() * 0.2f  // slight random pitch variation
        );
        var start = player.getEyePosition();
        var look  = player.getLookAngle();
        var end   = start.add(look.scale(RANGE));

        // collect all living entities in a box around the ray
        AABB searchBox = new AABB(start, end).inflate(1.0);
        List<LivingEntity> entities = world.getEntitiesOfClass(
                LivingEntity.class, searchBox,
                e -> e != player && e.isAlive()
        );

        // find the closest one that intersects the ray
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
            float damage = target instanceof SquirrelEntity ? SQUIRREL_DAMAGE : OTHER_DAMAGE;
            target.hurt(net.minecraft.world.damagesource.DamageSource.playerAttack(player), damage);
        }
    }

    // call this when giving the gun to a player so it starts loaded
    public static ItemStack createLoaded(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.getOrCreateTag().putBoolean(LOADED_KEY, true);
        return stack;
    }
    @Override
    public void verifyTagAfterLoad(net.minecraft.nbt.CompoundTag tag) {
        if (!tag.contains(LOADED_KEY)) {
            tag.putBoolean(LOADED_KEY, true);
        }
    }
}