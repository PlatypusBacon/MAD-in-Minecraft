// ModCombatEvents.java
package bunger.group.tyler;

import bunger.group.tyler.combat.ParryHandler;
import bunger.group.tyler.enchantment.ModEnchantments;
import bunger.group.tyler.item.BearBoxersItem;
import bunger.group.tyler.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModCombatEvents {
    private static final Map<UUID, Long> LAST_PUNCH_TIME = new HashMap<>();
    private static final float BEAR_BOXERS_DAMAGE = 5.0f;
    private static final long PUNCH_COOLDOWN_TICKS = 8L;
    public static void register() {
        AttackEntityCallback.EVENT.register(ModCombatEvents::onAttack); // single registration
        ParryHandler.register();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack off  = player.getItemInHand(InteractionHand.OFF_HAND);
                boolean mainIsBears = main.is(ModItems.BEAR_BOXERS);
                boolean offIsBears  = off.is(ModItems.BEAR_BOXERS);
                if (mainIsBears && !offIsBears) {
                    player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ModItems.BEAR_BOXERS));
                } else if (!mainIsBears && offIsBears) {
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer player) {
                if (player.getItemInHand(InteractionHand.OFF_HAND).is(ModItems.BEAR_BOXERS)) {
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        });
    }

    private static InteractionResult onAttack(Player attacker, Level level,
                                              InteractionHand hand, Entity entity,
                                              @Nullable EntityHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;

        ItemStack stack = attacker.getItemInHand(hand);
        if (stack.isEmpty()) return InteractionResult.PASS;


        if (stack.is(ModItems.BEAR_BOXERS)) {
            UUID id = attacker.getUUID();
            long now = level.getGameTime();

            if (now - LAST_PUNCH_TIME.getOrDefault(id, 0L) < PUNCH_COOLDOWN_TICKS) {
                return InteractionResult.SUCCESS; // on cooldown, cancel
            }
            LAST_PUNCH_TIME.put(id, now);

            BearBoxersItem.onPunch(attacker);

            if (entity instanceof LivingEntity target && level instanceof ServerLevel serverLevel) {
                // createAttackSource is private — use damageSources().playerAttack() directly
                DamageSource source = level.damageSources().playerAttack(attacker);

                float oldHealth = target.getHealth();
                Vec3 oldMovement = entity.getDeltaMovement();

                boolean wasHurt = target.hurtServer(serverLevel, source, BEAR_BOXERS_DAMAGE);

                if (wasHurt) {
                    // causeExtraKnockback is public on Player
                    float knockback = (float) attacker.getAttributeValue(
                            Attributes.ATTACK_KNOCKBACK);
                    attacker.causeExtraKnockback(entity, knockback, oldMovement);

                    attacker.setLastHurtMob(entity);
                    attacker.causeFoodExhaustion(0.1f);
                    // Brief speed boost after landing a hit
                    attacker.addEffect(new MobEffectInstance(
                            MobEffects.SPEED,
                            40,  // 2 seconds
                            0,
                            false, // not ambient
                            false, // no particles (subtle)
                            false  // no icon in HUD
                    ));
                    // damageStatsAndHearts is private — replicate it manually
                    float actualDamage = oldHealth - target.getHealth();
                    if (actualDamage > 0f) {
                        attacker.awardStat(Stats.DAMAGE_DEALT,
                                Math.round(actualDamage * 10f));
                        if (actualDamage > 2f) {
                            int count = (int)(actualDamage * 0.5f);
                            serverLevel.sendParticles(
                                    net.minecraft.core.particles.ParticleTypes.DAMAGE_INDICATOR,
                                    entity.getX(),
                                    entity.getY(0.5),
                                    entity.getZ(),
                                    count, 0.1, 0.0, 0.1, 0.2);
                        }
                    }

                    // attackVisualEffects is private — replicate the strong hit sound only
                    // (no crit, no sweep, no magic boost for brass knuckles)
                    level.playSound(
                            null,
                            attacker.getX(), attacker.getY(), attacker.getZ(),
                            SoundEvents.PLAYER_ATTACK_STRONG,
                            attacker.getSoundSource(),
                            1.0f, 1.0f);

                } else {
                    level.playSound(
                            null,
                            attacker.getX(), attacker.getY(), attacker.getZ(),
                            SoundEvents.PLAYER_ATTACK_NODAMAGE,
                            attacker.getSoundSource(),
                            1.0f, 1.0f);
                }
            }

            return InteractionResult.SUCCESS; // cancel vanilla player.attack()
        }

        // Stapler enchantment
        if (!hasStapeledEnchantment(level, stack)) return InteractionResult.PASS;
        if (entity instanceof Player target) return tryStaplePlayer(attacker, hand, target, stack);
        if (entity instanceof Mob mob) return tryStapleMob(attacker, stack, mob);

        return InteractionResult.PASS;
    }

    private static InteractionResult tryStapleMob(Player attacker, ItemStack stack, Mob mob) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) return InteractionResult.PASS;

        EquipmentSlot slot = equippable.slot();
        // Only equip if the slot is empty
        if (!mob.getItemBySlot(slot).isEmpty()) return InteractionResult.PASS;
        if (!mob.isAlive()) return InteractionResult.PASS;

        mob.setItemSlot(slot, stack.split(1));
        // Prevent the mob from dropping the item
        mob.setDropChance(slot, 0f);
        return InteractionResult.SUCCESS;
    }
    private static InteractionResult tryStaplePlayer(Player attacker, InteractionHand hand,
                                               Player target, ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) return InteractionResult.PASS;

        // equipOnTarget checks: isEquippableInSlot, !hasItemInSlot, isAlive
        InteractionResult result = equippable.equipOnTarget(attacker, target, stack);

        if (result == InteractionResult.SUCCESS) {
            return result;
        }

        return InteractionResult.PASS;
    }

    private static boolean hasStapeledEnchantment(Level level, ItemStack stack) {
        Holder<Enchantment> enchantment = getStapeledHolder(level);
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0;
    }

    private static Holder<Enchantment> getStapeledHolder(Level level) {
        Registry<Enchantment> registry =
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        return registry.get(ModEnchantments.STAPELED)
                .orElseThrow(() -> new IllegalStateException("Missing STAPELED enchantment"));
    }

}