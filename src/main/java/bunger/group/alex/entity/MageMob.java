package bunger.group.alex.entity;

import bunger.group.alex.entity.goal.MediumCastGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class MageMob extends Monster {

    @Nullable
    private Vec3 castTarget = null;

    public Vec3 getCastTarget() { return castTarget; }
    public void setCastTarget(@Nullable Vec3 pos) { this.castTarget = pos; }

    protected MageMob(EntityType<? extends MageMob> entityType, Level world) {
        super(entityType, world);
    }
}