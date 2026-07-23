package bunger.group.client.tyler.net;

import bunger.group.tyler.item.BearBoxersItem;
import bunger.group.tyler.net.PunchSidePacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PunchSidePacketClient {

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PunchSidePacket.TYPE, (payload, context) ->
                context.client().execute(() ->
                        BearBoxersItem.PUNCH_SIDE_CLIENT.put(
                                payload.playerUUID(), payload.useOffhand()
                        )
                )
        );
    }
}