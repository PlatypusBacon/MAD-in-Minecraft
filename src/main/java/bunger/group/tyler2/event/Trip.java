package bunger.group.tyler2.event;

import bunger.group.tyler.sound.ModSounds;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Trip {

    private static final Map<UUID, BlockPos> lastPositions = new HashMap<>();
    private static final float DROP_CHANCE = 0.00001f;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.isSpectator()) continue;

                BlockPos currentPos = player.blockPosition();
                BlockPos lastPos = lastPositions.get(player.getUUID());

                if (lastPos != null && !currentPos.equals(lastPos)) {
                    if (player.getRandom().nextFloat() < DROP_CHANCE) {
                        dropEntireInventory(player);
                    }
                }

                lastPositions.put(player.getUUID(), currentPos);
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                lastPositions.remove(handler.player.getUUID())
        );
    }

    private static void dropEntireInventory(ServerPlayer player) {
        // ouch but no ouch
        player.indicateDamage(0, 0);
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                net.minecraft.sounds.SoundEvents.PLAYER_HURT,
                player.getSoundSource(),
                1.0f,
                1.0f
        );
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                ModSounds.SLIP,
                player.getSoundSource(),
                0.4f,  // quieter so it doesn't overpower the hurt sound
                1.8f   // high pitch to make it feel more like a slip/click
        );
        player.sendSystemMessage(
                Component.literal("Whoops! You slipped"));
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                // drop() handles scatter velocity and stats tracking
                player.drop(stack.copy(), true, false);
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }
}