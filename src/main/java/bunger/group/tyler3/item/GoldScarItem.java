package bunger.group.tyler3.item;

import bunger.group.tyler3.sounds.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.List;


public class GoldScarItem extends Item {
    private static final String LOADED_KEY = "Loaded";
    private static final int RELOAD_TICKS = 60;
    private static final float DAMAGE = 3F;
    private static final double RANGE = 32.0;
    private static final String CLIP_KEY = "Clip";
    private static final int MAX_CLIP = 30;
    public GoldScarItem(Properties properties) {
        super(properties);
    }

    private int getClip(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return 0;
        return data.copyTag().getInt(CLIP_KEY).orElse(0);
    }

    private void setClip(ItemStack stack, int value) {
        CompoundTag tag = stack.has(DataComponents.CUSTOM_DATA)
                ? stack.get(DataComponents.CUSTOM_DATA).copyTag()
                : new CompoundTag();
        tag.putInt(CLIP_KEY, value);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public ItemUseAnimation getUseAnimation(final ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    private float getRecoilProgress(ItemStack stack, Level world) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return 0f;

        long last = data.copyTag().getLong(LAST_SHOT_KEY).orElse(0L);
        long age = world.getGameTime() - last;

        if (age < 2) return 1.0f;   // full recoil
        if (age < 5) return 0.0f;   // quick return window if needed
        return 0.0f;
    }

    @Override
    public int getUseDuration(final ItemStack stack, final LivingEntity entity) {
        return 5;
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

    private int countAmmo(Player player) {
        int total = 0;

        for (ItemStack invStack : player.getInventory().getNonEquipmentItems()) {
            if (invStack.getItem() instanceof MediumAmmoItem) {
                total += invStack.getCount();
            }
        }

        return total;
    }

    private boolean consumeAmmo(Player player) {
        for (ItemStack invStack : player.getInventory().getNonEquipmentItems()) {
            if (invStack.getItem() instanceof MediumAmmoItem) {
                invStack.shrink(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int clip = getClip(stack);
        return Math.round(13.0F * clip / MAX_CLIP);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFFD700; // gold color
    }
    private static final String LAST_SHOT_KEY = "LastShot";

    private void markShot(ItemStack stack, Level world) {
        CompoundTag tag = stack.has(DataComponents.CUSTOM_DATA)
                ? stack.get(DataComponents.CUSTOM_DATA).copyTag()
                : new CompoundTag();
        tag.putLong(LAST_SHOT_KEY, world.getGameTime());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private boolean isFiring(ItemStack stack, Level world) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;

        long last = data.copyTag().getLong(LAST_SHOT_KEY).orElse(0L);
        return world.getGameTime() - last < 2; // 2 ticks recoil
    }
    @Override
    public @NonNull InteractionResult use(Level world, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        if (isLoaded(stack)) {
            if (!consumeAmmo(player)) {
                player.sendSystemMessage(Component.literal("Out of ammo!"));
                return InteractionResult.FAIL;
            }
            markShot(stack, world);
            shoot(world, player);
            int clip = getClip(stack);
            clip--;
            setClip(stack, clip);

            if (clip <= 0) {
                setLoaded(stack, false);
            }
            player.getCooldowns().addCooldown(stack, 1);
        } else {
            setLoaded(stack, true);
            setClip(stack, 30);
            player.getCooldowns().addCooldown(stack, RELOAD_TICKS);
            player.sendSystemMessage(Component.literal("Reloading..."));
            world.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.SCAR_RELOAD,
                    SoundSource.PLAYERS,
                    1.0f,
                    0.9f + world.getRandom().nextFloat() * 0.2f
            );
        }

        return InteractionResult.CONSUME;
    }

    private void shoot(Level world, Player player) {

        world.playSound(
                null,
                player.blockPosition(),
                ModSounds.SCAR_SHOT,
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
            target.hurt(world.damageSources().playerAttack(player), DAMAGE);
        }
        player.push(-look.x * 0.1, 0.05, -look.z * 0.1);
    }

    public static ItemStack createLoaded(Item item) {
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(LOADED_KEY, true);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }
}
