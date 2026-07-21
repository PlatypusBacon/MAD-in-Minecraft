package bunger.group.tyler3.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class DeerEntity extends Animal
{
    public static AttributeSupplier.Builder createAttributes()
    {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
        .add(Attributes.FOLLOW_RANGE, 36.0)
          .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    public DeerEntity(EntityType<? extends Animal> entityType, Level world)
    {
        super(entityType, world);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(0, new ClimbOnTopOfPowderSnowGoal(this, level()));

        goalSelector.addGoal(2, new DeerBeingSerious<>(this, Player.class, 20f, 1.8, 2.6));
        goalSelector.addGoal(2, new DeerGetingFunny(this, 1.5, true));
        //goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new DeerBeingFunny<>(this, Player.class, true));
        /*
        goalSelector.addGoal(3, new TemptGoal(
                this, 1.25,
                stack -> stack.is(ModTags.DEER_FOOD),
                false
        ));
        */
        goalSelector.addGoal(4, eatGrassGoal = new EatBlockGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0f, 1));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1));
    }

    @Override
    public boolean isFood(ItemStack stack)
    {
        return false;
        //return stack.is(ModTags.DEER_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity)
    {
        //return (AgeableMob) BuiltInRegistries.ENTITY_TYPE.getValue(ModResourceLocation.of("deer")).create(world, EntitySpawnReason.BREEDING);
        return null;
    }



    // EATING GRASS

    public final AnimationState eatGrassAnimationState = new AnimationState();
    private EatBlockGoal eatGrassGoal;
    private int eatGrassTimer = 0;

    @Override
    protected void customServerAiStep(ServerLevel world)
    {
        eatGrassTimer = eatGrassGoal.getEatAnimationTick();
        super.customServerAiStep(world);
    }

    @Override
    public void aiStep()
    {
        if (level().isClientSide())
            eatGrassTimer = Math.max(0, eatGrassTimer - 1);

        super.aiStep();
    }

    @Override
    public void tick()
    {
        super.tick();
        updateEatGrassAnimation();
    }

    @Override
    public void handleEntityEvent(byte status)
    {
        if (status == EntityEvent.EAT_GRASS)
            eatGrassTimer = 40;

        super.handleEntityEvent(status);
    }

    @Override
    public void ate()
    {
        super.ate();
        if (isBaby()) ageUp(60);
    }

    private void updateEatGrassAnimation()
    {
        if (eatGrassTimer > 0)
            eatGrassAnimationState.startIfStopped(tickCount);
        else
            eatGrassAnimationState.stop();
    }
}
