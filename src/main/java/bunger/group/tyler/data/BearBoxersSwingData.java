package bunger.group.tyler.data;

import net.minecraft.world.InteractionHand;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BearBoxersSwingData {
    public boolean swinging;
    public InteractionHand swingingArm = InteractionHand.MAIN_HAND;
    public float attackAnim;
    public float attackAnimO;
    public int swingTime;

    // Tracks which hand to use NEXT punch (alternator)
    public static final Map<UUID, Boolean> PUNCH_SIDE_NEXT = new HashMap<>();
    // Tracks which hand is CURRENTLY swinging (renderer reads this)
    public static final Map<UUID, Boolean> PUNCH_SIDE_CURRENT = new HashMap<>();

    private static final Map<UUID, BearBoxersSwingData> DATA = new HashMap<>();

    public static BearBoxersSwingData get(UUID uuid) {
        return DATA.computeIfAbsent(uuid, k -> new BearBoxersSwingData());
    }

    public static void remove(UUID uuid) {
        DATA.remove(uuid);
    }
}