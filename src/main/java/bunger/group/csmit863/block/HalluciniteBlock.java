package bunger.group.csmit863.block;

import bunger.group.csmit863.entity.ModEntityTypes;
import bunger.group.csmit863.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HalluciniteBlock extends Block {
    private static final int EFFECT_RANGE = 15;
    private static final int EFFECT_DURATION = 300; // ticks, refreshed every random tick

    public HalluciniteBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
        }

        // 10% chance per random tick to spawn a shroomjak
        if (random.nextFloat() < 0.05f) {
            ModEntityTypes.SHROOMJAK.spawn(
                    level,
                    pos.above(),
                    EntitySpawnReason.TRIGGERED
            );
        }
    }
}