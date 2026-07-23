package bunger.group.mixin;

import bunger.group.alex.item.armour.SkeletonChestplate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(method = "applyOnProjectileSpawned", at = @At("TAIL"))
    private void onSpawned(ServerLevel level, ItemStack weapon, CallbackInfo ci) {
        Projectile self = (Projectile)(Object)this;
        if (!(self instanceof AbstractArrow arrow)) return;

        Entity owner = arrow.getOwner();
        if (!(owner instanceof Player player)) return;

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        try {
            double current = (double) BASE_DAMAGE_FIELD.get(arrow);
            if (chest.getItem() instanceof SkeletonChestplate
                    && !arrow.entityTags().contains("skeleton_chest_buffed")) {
                BASE_DAMAGE_FIELD.set(arrow, current * 1.25);
                arrow.addTag("skeleton_chest_buffed");
            }
            // System.out.println("[ArrowDebug] Damage: " + BASE_DAMAGE_FIELD.get(arrow));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final java.lang.reflect.Field BASE_DAMAGE_FIELD;
    static {
        try {
            BASE_DAMAGE_FIELD = AbstractArrow.class.getDeclaredField("baseDamage");
            BASE_DAMAGE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Could not find baseDamage field", e);
        }
    }
}