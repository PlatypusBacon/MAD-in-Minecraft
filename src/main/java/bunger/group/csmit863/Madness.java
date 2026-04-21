package bunger.group.csmit863;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class Madness {
    private static final AttachmentType<Integer> CURRENT_MADNESS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "current_madness"),
            builder -> builder
                    .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
                    .copyOnDeath() // keep current mana on death, probably the right play
    );
    private static final AttachmentType<Integer> MAX_MADNESS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "max_madness"),
            builder -> builder
                    .syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );

    public static bunger.group.csmit863.Madness.MadnessData get(AttachmentTarget target) {
        return new bunger.group.csmit863.Madness.MadnessData(target);
    }

    public record MadnessData(AttachmentTarget target) {
        public int getCurrentMana() {
            return target.getAttachedOrElse(CURRENT_MADNESS, 0);
        }

        public int incrementCurrentMadness() {
            int current = target.getAttachedOrElse(CURRENT_MADNESS, 0);
            int newValue = current + 1;
            target.setAttached(CURRENT_MADNESS, newValue);
            return newValue;
        }

        public void useMadness(int value) {
            this.setCurrentMadness(getCurrentMana()-value);
        }

        public void setCurrentMadness(int value) {
            target.setAttached(CURRENT_MADNESS, value);
        }

        public int getMaxMadness() {
            return target.getAttachedOrElse(MAX_MADNESS, 0);
        }

        public void setMaxMadness(int value) {
            target.setAttached(MAX_MADNESS, value);
        }

        public void recalculateMaxMana() {
            if (target instanceof Player player) {

                // Okay so this is funky but best way (i think)
                int maxMana = 100; // base mana, then add bonus then set it?


                // then redefine it as Max Mana
                this.setMaxMadness(maxMana);

            } else {
                System.out.println("UHHHHHHHH");// REALLY shouldn't get here
            }
        }
    }
}