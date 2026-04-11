package bunger.group.ethan;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ProphetEntity extends PathfinderMob {

    public ProphetEntity(EntityType<? extends ProphetEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 4.0F));
    }

    public static void initialize() {

    }

}