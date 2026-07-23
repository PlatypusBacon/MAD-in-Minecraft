package bunger.group.tyler3;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;

public class StripperEvent {

    public static final Event<Detected> DETECTED = EventFactory.createArrayBacked(Detected.class,
            listeners -> (player, pos, direction, streakLength) -> {
                for (Detected listener : listeners) {
                    listener.onStripMiningDetected(player, pos, direction, streakLength);
                }
            });

    private StripperEvent() {}

    @FunctionalInterface
    public interface Detected {
        /**
         * @param player       the player who triggered the detection
         * @param position     the position of the most recently mined block
         * @param direction    the horizontal direction they're tunneling in
         * @param streakLength how many blocks long the current straight-line streak is
         */
        void onStripMiningDetected(ServerPlayer player, BlockPos position, Direction direction, int streakLength);
    }
}