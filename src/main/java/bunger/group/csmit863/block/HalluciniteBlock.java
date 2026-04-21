package bunger.group.csmit863.block;

import bunger.group.csmit863.entity.ModEntityTypes;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HalluciniteBlock extends Block {
    private static final int EFFECT_RANGE = 15;
    private static final int EFFECT_DURATION = 300; // ticks, refreshed every random tick
    private static final int SPREAD_RANGE = 3;
    public HalluciniteBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private void trySpread(ServerLevel level, BlockPos pos, RandomSource random) {
        // pick a random nearby block
        BlockPos target = pos.offset(
                random.nextInt(SPREAD_RANGE * 2 + 1) - SPREAD_RANGE,
                random.nextInt(3) - 1,
                random.nextInt(SPREAD_RANGE * 2 + 1) - SPREAD_RANGE
        );

        BlockState targetState = level.getBlockState(target);
        Block targetBlock = targetState.getBlock();

        boolean isConvertible = targetState.is(Blocks.DIRT)
                || targetState.is(Blocks.GRASS_BLOCK)
                || targetState.is(Blocks.COARSE_DIRT)
                || targetState.is(Blocks.ROOTED_DIRT)
                || targetState.is(BlockTags.LOGS)
                || targetState.is(BlockTags.PLANKS);

        if (isConvertible) {
            level.setBlock(target, ModBlocks.HALLUCINITE_BLOCK.defaultBlockState(), 3);
        }
    }


    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // apply hallucination to nearby players
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
        if (random.nextFloat() < 1.0f) {
            trySpread(level, pos, random);
        }
    }
}