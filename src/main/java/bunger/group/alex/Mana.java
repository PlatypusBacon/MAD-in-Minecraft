package bunger.group.alex;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.effect.ManaBoostEffect;
import bunger.group.alex.effect.ModEffects;
import bunger.group.alex.item.armour.ClothArmour;
import bunger.group.alex.item.armour.EnchantedChainArmour;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Mana {
    private static final AttachmentType<Integer> CURRENT_MANA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "current_mana"),
            builder -> builder
                    .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
                    .copyOnDeath() // keep current mana on death, probably the right play
    );
    private static final AttachmentType<Integer> MAX_MANA = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "max_mana"),
            builder -> builder
                    .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );

    public static ManaData get(AttachmentTarget target) {
        return new ManaData(target);
    }

    public record ManaData(AttachmentTarget target) {
        public int getCurrentMana() {
            return target.getAttachedOrElse(CURRENT_MANA, 0);
        }

        public int incrementCurrentMana() {
            int current = target.getAttachedOrElse(CURRENT_MANA, 0);
            int newValue = current + 1;
            target.setAttached(CURRENT_MANA, newValue);
            return newValue;
        }

        public void useMana(int value) {
            this.setCurrentMana(getCurrentMana()-value);
        }

        public void setCurrentMana(int value) {
            target.setAttached(CURRENT_MANA, value);
        }

        public int getMaxMana() {
            return target.getAttachedOrElse(MAX_MANA, 0);
        }

        public void setMaxMana(int value) {
            target.setAttached(MAX_MANA, value);
        }

        public void recalculateMaxMana() {
            if (target instanceof Player player) {
                // Okay so this is funky but best way (i think)
                int maxMana = 50; // base mana,

                // Items
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack stack = player.getItemBySlot(slot);

                    // Cloth Armour
                    if (stack.getItem() instanceof ClothArmour armor) {
                        if (armor.getSlot() == slot) {
                            maxMana += armor.getMaxManaBonus();
                        }
                    }

                    // Enchanted Armour
                    if (stack.getItem() instanceof EnchantedChainArmour armor) {
                        if (armor.getSlot() == slot) {
                            maxMana += armor.getMaxManaBonus();
                        }
                    }
                }

                // Potion effect
                MobEffectInstance effect = player.getEffect(ModEffects.MANA_BOOST);
                if (effect != null) {
                    int amplifier = effect.getAmplifier();
                    maxMana += ManaBoostEffect.BOOST * (1+ amplifier);
                }

                // then redefine it as Max Mana
                this.setMaxMana(maxMana);
                if (this.getCurrentMana() > this.getMaxMana()) {
                    this.setCurrentMana(maxMana);
                    ServerPlayNetworking.send((ServerPlayer) player, new ManaPacket(this.getCurrentMana(), this.getMaxMana()));
                }

            } else {
                System.out.println("UHHHHHHHH");// REALLY shouldn't get here
            }
        }
    }
}