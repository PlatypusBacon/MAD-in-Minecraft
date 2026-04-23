package bunger.group.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.CustomSounds;
import bunger.group.csmit863.block.ModBlocks;
import bunger.group.csmit863.effects.HallucinationEffect;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ShroomjakEntity extends Animal {
    public ShroomjakEntity(Level world) {
        this(ModEntityTypes.SHROOMJAK, world);
    }
    private static final int SPRAY_RANGE = 3; // blocks away
    private static final int SPRAY_COOLDOWN = 100; // ticks between sprays
    private int sprayCooldown = 0;
    public ShroomjakEntity(EntityType<? extends ShroomjakEntity> entityType, Level world) {
        super(entityType, world);  // Animal's constructor takes the same params, no change needed
    }
    private static final int BONEMEAL_COOLDOWN = 300; // 200 ticks = 10 seconds
    private int bonemealCooldown = 0;

    public static AttributeSupplier.Builder createCubeAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.FOLLOW_RANGE, 50);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1, Ingredient.of(Items.ROTTEN_FLESH), false));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, true)); // attack

        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        // this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.ROTTEN_FLESH);
    }

    @Override
    public ShroomjakEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return ModEntityTypes.SHROOMJAK.create(level, EntitySpawnReason.BREEDING);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (this.random.nextFloat() < 0.7f) {
            this.spawnAtLocation(level, ModItems.MAGIC_MUSHROOM);
        }
    }

    private void plantNearby(
            int radius,
            float probability,
            Block block,
            ServerLevel serverLevel
    ){
        // randomly place magic mushrooms in nearby positions
        for (int i = 0; i < radius; i++) {
            if (this.random.nextFloat() < probability) {// 0.2f
                BlockPos mushroomPos = this.blockPosition().offset(
                        this.random.nextInt(9) - radius,
                        0,
                        this.random.nextInt(9) - radius
                );
                BlockState above = serverLevel.getBlockState(mushroomPos);
                BlockState below = serverLevel.getBlockState(mushroomPos.below());
                if (
                        above.isAir() &&
                                (below.isSolidRender() && (
                                        below.is(Blocks.GRASS_BLOCK) ||
                                                    below.is(Blocks.DIRT)
                                        ))) {
                    serverLevel.setBlock(mushroomPos, block.defaultBlockState(), 3); // ModBlocks.MAGIC_MUSHROOM_BLOCK.defaultBlockState()
                }
            }
        }
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
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, newDuration, 1));
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 500, 1));


                    // Spray particles, play fart sound
                    this.playSound(CustomSounds.SHROOMJAK_FART1, 0.3F, 0.8F);
                    ServerLevel serverLevel = (ServerLevel) level();
                    serverLevel.sendParticles(
                            ParticleTypes.LARGE_SMOKE, // or whatever particle fits
                            this.getX(), this.getY() + 1, this.getZ(),
                            200,   // count
                            1.5, 0.5, 1.5, // spread x/y/z
                            0.05  // speed
                    );

                    sprayCooldown = SPRAY_COOLDOWN;
                }
            }

            if (bonemealCooldown > 0) {
                bonemealCooldown--;
            }

            if (bonemealCooldown == 0) {
                ServerLevel serverLevel = (ServerLevel) level();

                // black particles from head, play plant sound
                this.playSound(CustomSounds.SHROOMJAK_PLANT, 0.3F, 1.0F);
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        this.getX(), this.getY() + 2, this.getZ(),
                        10, 0.2, 0.2, 0.2, 0.05
                );

                plantNearby(3, 0.2f, ModBlocks.MAGIC_MUSHROOM_BLOCK, serverLevel);
                plantNearby(8, 0.2f, Blocks.JUNGLE_SAPLING, serverLevel);
                plantNearby(8, 0.2f, Blocks.BROWN_MUSHROOM, serverLevel);

                // bonemeal everything in 2 block radius
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos nearPos = this.blockPosition().offset(x, 0, z);
                        BoneMealItem.growCrop(ItemStack.EMPTY, serverLevel, nearPos);
                        BoneMealItem.growCrop(ItemStack.EMPTY, serverLevel, nearPos.below());
                    }
                }

                bonemealCooldown = BONEMEAL_COOLDOWN;
            }
        }

        
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        this.playSound(CustomSounds.SHROOMJAK_ANGRY1, 0.3F, 0.9F);
    }

    @Override
    public void playHurtSound(DamageSource source) {
        this.playSound(CustomSounds.SHROOMJAK_ANGRY2, 0.3F, 0.9F);
    }

}