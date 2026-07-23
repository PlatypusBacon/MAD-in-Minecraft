package bunger.group.csmit863.block;

import bunger.group.csmit863.biome.ModBiomes;
import bunger.group.csmit863.entity.ModEntityTypes;
import bunger.group.csmit863.entity.ShroomjakEntity;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class HalluciniteBlock extends Block {
    private static final int EFFECT_RANGE = 15;
    private static final int EFFECT_DURATION = 300;

    public HalluciniteBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    private int countNearbyShroomjaks(ServerLevel level, BlockPos pos, int radius) {
        return level.getEntitiesOfClass(
                ShroomjakEntity.class,
                new AABB(pos).inflate(radius)
        ).size();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Decay check — runs before anything else; if decayed, block is gone
        if (!level.getBlockState(pos).is(ModBlocks.HALLUCINITE_BLOCK)) return;

        // Apply hallucination to nearby players
        List<Player> nearbyPlayers = level.getEntitiesOfClass(
                Player.class,
                new net.minecraft.world.phys.AABB(pos).inflate(EFFECT_RANGE)
        );
        for (Player player : nearbyPlayers) {
            player.addEffect(new MobEffectInstance(
                    ModItems.HALLUCINATION_EFFECT,
                    EFFECT_DURATION,
                    0,
                    true,
                    true
            ));
            player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, EFFECT_DURATION, 1));
        }

        // 5% chance per random tick to spawn a shroomjak
        if (random.nextFloat() < 0.05f) {
            // limit check first
            if (countNearbyShroomjaks(level, pos, 20) >= 5) {
                return;
            }
            // Find ground level near the spire base
            // Try random positions around the spire
            for (int attempts = 0; attempts < 10; attempts++) {
                int offsetX = random.nextInt(7) - 3;
                int offsetZ = random.nextInt(7) - 3;
                BlockPos candidate = pos.offset(offsetX, 0, offsetZ);

                // Walk down to find solid ground
                for (int dy = 3; dy >= -3; dy--) {
                    BlockPos ground = candidate.offset(0, dy, 0);
                    BlockPos above = ground.above();
                    if (level.getBlockState(ground).isSolid() &&
                            level.getBlockState(above).isAir() &&
                            level.getBlockState(above.above()).isAir()) {
                        ModEntityTypes.SHROOMJAK.spawn(
                                level,
                                above,
                                EntitySpawnReason.TRIGGERED
                        );
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3f) {
            // Pick a random side
            Direction side = Direction.values()[random.nextInt(Direction.values().length)];

            // Start at center of block face on that side
            double x = pos.getX() + 0.5 + side.getStepX() * 0.5;
            double y = pos.getY() + 0.5 + side.getStepY() * 0.5;
            double z = pos.getZ() + 0.5 + side.getStepZ() * 0.5;

            // Velocity shoots outward from that face
            double vx = side.getStepX() * 0.05 + (random.nextDouble() - 0.5) * 0.02;
            double vy = side.getStepY() * 0.05 + random.nextDouble() * 0.02;
            double vz = side.getStepZ() * 0.05 + (random.nextDouble() - 0.5) * 0.02;

            level.addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x, y, z,
                    vx, vy, vz
            );
        }
    }
}