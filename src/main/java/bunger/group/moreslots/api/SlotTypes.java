package bunger.group.moreslots.api;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.Mana;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SlotTypes {
    public static final AttachmentType<ItemStack> EQUIPMENT_SLOT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "slot_equipment"),
            builder -> builder
                    .persistent(ItemStack.OPTIONAL_CODEC)
                    .syncWith(ItemStack.OPTIONAL_STREAM_CODEC, AttachmentSyncPredicate.all())
    );

    public static final int SLOT_COUNT = 1;

    /*
    Registers data tag of each new slot for player if they don't have them currently, call in mod init
     */
    public static void initPlayer(ServerPlayer player) {
        if (player.getAttached(EQUIPMENT_SLOT) == null) {
            player.setAttached(EQUIPMENT_SLOT, ItemStack.EMPTY);
        }
    }

    public static SlotData get(AttachmentTarget target) {
        return new SlotData(target);
    }
}