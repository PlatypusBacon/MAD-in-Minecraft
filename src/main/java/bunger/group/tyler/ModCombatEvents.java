// ModCombatEvents.java
package bunger.group.tyler;

import bunger.group.tyler.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class ModCombatEvents {

    public static void register() {
        AttackEntityCallback.EVENT.register(ModCombatEvents::onAttack);
    }

    private static InteractionResult onAttack(Player attacker, Level level,
                                              InteractionHand hand, Entity entity,
                                              EntityHitResult hitResult) {
        if (level.isClientSide()) return InteractionResult.PASS;
        if (!(entity instanceof Player target)) return InteractionResult.PASS;

        ItemStack stack = attacker.getItemInHand(hand);
        if (stack.isEmpty()) return InteractionResult.PASS;

        if (!hasStapeledEnchantment(level, stack)) return InteractionResult.PASS;

        return tryStaple(attacker, hand, target, stack);
    }

    private static InteractionResult tryStaple(Player attacker, InteractionHand hand,
                                               Player target, ItemStack stack) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) return InteractionResult.PASS;

        // equipOnTarget checks: isEquippableInSlot, !hasItemInSlot, isAlive
        InteractionResult result = equippable.equipOnTarget(attacker, target, stack);

        if (result == InteractionResult.SUCCESS) {
            // equipOnTarget already called stack.split(1), consuming from attacker's hand
            // if hand is now empty we don't need to do anything extra
            // but if attacker had count > 1, stack is already shrunk by split()
            return InteractionResult.SUCCESS;
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