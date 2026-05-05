package bunger.group.tyler3.network;

import bunger.group.tyler3.item.GoldScarItem;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModNet {
    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(ReloadPacket.TYPE, ReloadPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ReloadPacket.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                Player player = context.player();
                ItemStack stack = player.getMainHandItem();
                if (!(stack.getItem() instanceof GoldScarItem)) return;
                if (player.getCooldowns().isOnCooldown(stack)) return;
                GoldScarItem.triggerReload(stack, player, context.server().overworld());
            });
        });
    }
}