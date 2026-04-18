package bunger.group.alex;


import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

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
            return target.modifyAttached(CURRENT_MANA, currentStamina -> currentStamina+1);
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
    }
}