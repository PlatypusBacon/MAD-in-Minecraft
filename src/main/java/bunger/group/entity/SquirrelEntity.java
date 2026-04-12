package bunger.group.entity;import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class SquirrelEntity extends Animal {

    public SquirrelEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)  // slightly faster base
                .add(Attributes.FOLLOW_RANGE, 32.0);   // extends detection/AI range
    }

    @Override
    protected void registerGoals() {
        // flee from players within 16 blocks, very fast
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 16f, 1.8, 2.4));
        // also flee from any mob that last hurt them
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, LivingEntity.class, 8f, 1.8, 2.4));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}