package bunger.group.tyler.item;

import bunger.group.tyler.entity.SquirrelBearEntity;
import bunger.group.tyler.entity.SquirrelEntity;
import bunger.group.tyler.sound.ModSounds;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;


import java.util.List;

public class SquirrelGunItem extends Item {

    private static final int RELOAD_TICKS = 60;
    private static final float SQUIRREL_DAMAGE = 50F;
    private static final float OTHER_DAMAGE = 4F;
    private static final double RANGE = 32.0;

    public static final DataComponentType<Boolean> LOADED =
            DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .build();

    public SquirrelGunItem(Properties settings) {
        super(settings);
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
        var look  = player.getLookAngle();
        var end   = start.add(look.scale(RANGE));

        AABB searchBox = new AABB(start, end).inflate(1.0);
        List<LivingEntity> entities = world.getEntitiesOfClass(
                LivingEntity.class, searchBox,
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
                    ? SQUIRREL_DAMAGE : OTHER_DAMAGE;
            target.hurt(world.damageSources().playerAttack(player), damage);
        }
    }

    public static ItemStack createLoaded(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.set(LOADED, true);
        return stack;
    }
}