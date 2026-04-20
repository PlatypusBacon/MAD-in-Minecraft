package bunger.group.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.block.ModBlocks;
import bunger.group.csmit863.effects.HallucinationEffect;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ShroomjakEntity extends PathfinderMob {
    public ShroomjakEntity(Level world) {
        this(ModEntityTypes.SHROOMJAK, world);
    }
    private static final int SPRAY_RANGE = 3; // blocks away
    private static final int SPRAY_COOLDOWN = 100; // ticks between sprays
    private int sprayCooldown = 0;
    public ShroomjakEntity(EntityType<? extends ShroomjakEntity> entityType, Level world) {
        super(entityType, world);
    }
    private static final int BONEMEAL_COOLDOWN = 300; // 200 ticks = 10 seconds
    private int bonemealCooldown = 0;

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
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (this.random.nextFloat() < 0.7f) {
            this.spawnAtLocation(level, ModItems.MAGIC_MUSHROOM);
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
                    nearestPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 5, 1));


                    // Spray particles
                    ServerLevel serverLevel = (ServerLevel) level();
                    serverLevel.sendParticles(
                            ParticleTypes.LARGE_SMOKE, // or whatever particle fits
                            this.getX(), this.getY() + 1, this.getZ(),
                            400,   // count
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

                // black particles from head
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        this.getX(), this.getY() + 2, this.getZ(),
                        10, 0.2, 0.2, 0.2, 0.05
                );

                // randomly place magic mushrooms in nearby positions
                for (int i = 0; i < 3; i++) {
                    if (this.random.nextFloat() < 0.2f) {
                        BlockPos mushroomPos = this.blockPosition().offset(
                                this.random.nextInt(9) - 4,
                                0,
                                this.random.nextInt(9) - 4
                        );
                        BlockState above = serverLevel.getBlockState(mushroomPos);
                        BlockState below = serverLevel.getBlockState(mushroomPos.below());
                        if (above.isAir() && (below.isSolidRender() || below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.MOSS_BLOCK))) {
                            serverLevel.setBlock(mushroomPos, ModBlocks.MAGIC_MUSHROOM_BLOCK.defaultBlockState(), 3);
                        }
                    }
                }

                // convert only the block directly beneath to moss first
                BlockPos directlyBelow = this.blockPosition().below();
                BlockState belowState = serverLevel.getBlockState(directlyBelow);
                if (belowState.is(Blocks.DIRT) || belowState.is(Blocks.GRASS_BLOCK)) {
                    serverLevel.setBlock(directlyBelow, Blocks.MOSS_BLOCK.defaultBlockState(), 3);
                }



                // then bonemeal everything in 2 block radius
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
}