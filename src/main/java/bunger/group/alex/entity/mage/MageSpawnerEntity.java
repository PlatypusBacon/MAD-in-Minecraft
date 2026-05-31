package bunger.group.alex.entity.mage;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class MageSpawnerEntity extends Monster {

    public MageSpawnerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
}