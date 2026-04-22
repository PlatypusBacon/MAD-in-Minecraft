package bunger.group.csmit863.block;

import bunger.group.csmit863.biome.ModBiomes;
import bunger.group.csmit863.entity.ModEntityTypes;
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
            player.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, EFFECT_DURATION, 1));
            player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, EFFECT_DURATION, 1));
        }

        // 1% chance per random tick to spawn a shroomjak
        if (random.nextFloat() < 0.01f) {
            ModEntityTypes.SHROOMJAK.spawn(
                    level,
                    pos.above(),
                    EntitySpawnReason.TRIGGERED
            );
        }

    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3f) { // adjust density
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;

            level.addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE, // or CAMPFIRE_COSY_SMOKE
                    x, y, z,
                    0.0, 0.05, 0.0
            );
        }
    }
}