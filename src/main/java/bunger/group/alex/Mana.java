package bunger.group.alex;


import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
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
                ItemStack helmet    = player.getItemBySlot(EquipmentSlot.HEAD);
                ItemStack chest     = player.getItemBySlot(EquipmentSlot.CHEST);
                ItemStack leggings  = player.getItemBySlot(EquipmentSlot.LEGS);
                ItemStack boots     = player.getItemBySlot(EquipmentSlot.FEET);

                // Okay so this is funky but best way (i think)
                int maxMana = 100; // base mana, then add bonus then set it?

                // Cloth Armour
                if (helmet.is(ModItems.CLOTH_HELMET)) {maxMana += 5;}
                if (chest.is(ModItems.CLOTH_CHESTPLATE)) {maxMana += 10;}
                if (leggings.is(ModItems.CLOTH_LEGGINGS)) {maxMana += 10;}
                if (boots.is(ModItems.CLOTH_BOOTS)) {maxMana += 5;}

                // then redefine it as Max Mana
                this.setMaxMana(maxMana);

            } else {
                System.out.println("UHHHHHHHH");// REALLY shouldn't get here
            }
        }
    }
}