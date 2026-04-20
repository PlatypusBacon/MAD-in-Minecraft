package bunger.group.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.effects.HallucinationEffect;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class ShroomjakEntity extends PathfinderMob {
    public ShroomjakEntity(Level world) {
        this(ModEntityTypes.SHROOMJAK, world);
    }
    private static final int SPRAY_RANGE = 3; // blocks away
    private static final int SPRAY_COOLDOWN = 40; // ticks between sprays
    private int sprayCooldown = 0;
    public ShroomjakEntity(EntityType<? extends ShroomjakEntity> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TemptGoal(this, 1, Ingredient.of(Items.BROWN_MUSHROOM), false));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Cow.class, 4));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }
    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            if (sprayCooldown > 0) {
                sprayCooldown--;
            }

            if (sprayCooldown == 0) {
                Player nearestPlayer = level().getNearestPlayer(this, SPRAY_RANGE);
                if (nearestPlayer != null) {
                    // Give hallucination effect - replace with your actual MobEffect
                    var newDuration = 1000;
                    var newAmp = 5;
                    nearestPlayer.addEffect(new MobEffectInstance(ModItems.HALLUCINATION_EFFECT, newDuration, newAmp));
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, newDuration, newAmp));
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.NAUSEA, newDuration, newAmp));
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, newDuration, newAmp));


                    // Spray particles
                    ServerLevel serverLevel = (ServerLevel) level();
                    serverLevel.sendParticles(
                            ParticleTypes.SPIT, // or whatever particle fits
                            this.getX(), this.getY() + 1, this.getZ(),
                            400,   // count
                            1.5, 0.5, 1.5, // spread x/y/z
                            0.05  // speed
                    );

                    sprayCooldown = SPRAY_COOLDOWN;
                }
            }
        }
    }
}