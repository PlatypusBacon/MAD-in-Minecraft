package bunger.group.alex.entity.goblin;

import org.jetbrains.annotations.Nullable;

/**
 * Implemented by any goblin that can be part of a patrol.
 * Gives them a reference back to their patrol anchor.
 */
public interface GoblinPatrolMember {
    void setPatrol(@Nullable GoblinChiefEntity patrol);
    @Nullable GoblinChiefEntity getPatrol();
}