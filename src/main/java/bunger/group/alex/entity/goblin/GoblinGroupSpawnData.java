package bunger.group.alex.entity.goblin;

import net.minecraft.world.entity.SpawnGroupData;

/**
 * Passed as groupData to any goblin spawned as part of a patrol.
 * Prevents them from triggering their own spawn logic.
 */
public class GoblinGroupSpawnData implements SpawnGroupData {
}