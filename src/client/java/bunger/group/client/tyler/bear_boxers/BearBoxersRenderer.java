package bunger.group.client.tyler.bear_boxers;

import bunger.group.tyler.item.ModItems;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BearBoxersRenderer {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            Player player = client.player;
            ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack off  = player.getItemInHand(InteractionHand.OFF_HAND);

            // If wearing boxers in main hand but offhand is empty, fill it client-side
            // visually. The server-side fill is handled via packet / inventory event.
            if (main.is(ModItems.BEAR_BOXERS) && off.isEmpty()) {
                // Visual hint only — actual equip must be server-authoritative
                // See note below about server-side offhand sync
            }
        });
    }
}