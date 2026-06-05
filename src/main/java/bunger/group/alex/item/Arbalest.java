package bunger.group.alex.item;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Arbalest extends CrossbowItem {

    private static final float CHARGE_SECONDS = 20.0F;
    private static final float ARROW_DAMAGE = 3.0F;

    private boolean startSoundPlayed = false;
    private boolean midSoundPlayed = false;

    public Arbalest(Item.Properties properties) {
        super(properties);
    }

    private int getMyChargeTicks(ItemStack stack, LivingEntity user) {
        float duration = EnchantmentHelper.modifyCrossbowChargingTime(stack, user, CHARGE_SECONDS);
        return Mth.floor(duration * 20.0F);
    }

    @Override
    public void performShooting(final Level level, final LivingEntity shooter, final InteractionHand hand, final ItemStack weapon, float power, float uncertainty, final @org.jspecify.annotations.Nullable LivingEntity targetOverride) {
        if (level instanceof ServerLevel serverLevel) {
            power *= 4.0F; // big Power
            uncertainty /= 2; // More accurate

            ChargedProjectiles charged = (ChargedProjectiles)weapon.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
            if (charged != null && !charged.isEmpty()) {
                this.shoot(serverLevel, shooter, hand, weapon, charged.itemCopies(), power, uncertainty, shooter instanceof Player, targetOverride);
                if (shooter instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer)shooter;
                    CriteriaTriggers.SHOT_CROSSBOW.trigger(player, weapon);
                    player.awardStat(Stats.ITEM_USED.get(weapon.getItem()));
                }

            }
        }
    }


    // The same, more power
    @Override
    protected void shoot(final ServerLevel level, final LivingEntity shooter, final InteractionHand hand, final ItemStack weapon, final List<ItemStack> projectiles, final float power, final float uncertainty, final boolean isCrit, final @org.jspecify.annotations.Nullable LivingEntity targetOverride) {
        float maxAngle = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
        float angleStep = projectiles.size() == 1 ? 0.0F : 2.0F * maxAngle / (float)(projectiles.size() - 1);
        float angleOffset = (float)((projectiles.size() - 1) % 2) * angleStep / 2.0F;
        float direction = 1.0F;

        for(int i = 0; i < projectiles.size(); ++i) {
            ItemStack projectile = (ItemStack)projectiles.get(i);
            if (!projectile.isEmpty()) {
                float angle = angleOffset + direction * (float)((i + 1) / 2) * angleStep;
                direction = -direction;
                int finalI = i;
                Projectile.spawnProjectile(this.createProjectile(level, shooter, weapon, projectile, isCrit), level, projectile, (projectileEntity) -> {
                    this.shootProjectile(shooter, projectileEntity, finalI, power, uncertainty, angle, targetOverride);
                    if (projectileEntity instanceof AbstractArrow arrow) {
                        arrow.setBaseDamage(ARROW_DAMAGE);
                    }
                });
                weapon.hurtAndBreak(this.getDurabilityUse(projectile), shooter, hand.asEquipmentSlot());
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (entity instanceof LivingEntity living && (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)) {
            living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 1, 3, false, false));
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (level.isClientSide()) return;

        int chargeTicks = getMyChargeTicks(stack, entity);
        int ticksHeld = this.getUseDuration(stack, entity) - ticksRemaining;
        float tickPercent = (float) ticksHeld / chargeTicks;

        if (tickPercent < 0.2F) {
            this.startSoundPlayed = false;
            this.midSoundPlayed = false;
        }

        if (tickPercent >= 0.2F && !this.startSoundPlayed) {
            this.startSoundPlayed = true;
            level.playSound((Entity) null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 0.5F, 1.0F);
        }

        if (tickPercent >= 0.5F && !this.midSoundPlayed) {
            this.midSoundPlayed = true;
            level.playSound((Entity) null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 0.5F, 1.0F);
        }

        if (tickPercent >= 1.0F && !isCharged(stack)) {
            super.onUseTick(level, entity, stack, this.getUseDuration(stack, entity) - chargeTicks);

            level.playSound((Entity) null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.CROSSBOW_LOADING_END, entity.getSoundSource(), 1.0F,
                    1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }
}