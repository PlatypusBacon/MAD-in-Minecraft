package bunger.group.tyler3.item;

import bunger.group.tyler3.block.PlatformTrackBlock;
import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler3.entity.PlatformEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PlatformItem extends Item {

    public PlatformItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        // Must be placed on a track block
        if (!(state.getBlock() instanceof PlatformTrackBlock)) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        ServerLevel serverLevel = (ServerLevel) level;

        // Check nothing already on this track pos
        boolean occupied = !serverLevel.getEntitiesOfClass(
                PlatformEntity.class,
                new AABB(pos).inflate(0.5),
                PlatformEntity::isCarrier
        ).isEmpty();

        if (occupied) return InteractionResult.FAIL;

        // Spawn carrier entity centred on the track block
        PlatformEntity platform = ModEntities.PLATFORM.create(
                serverLevel, EntitySpawnReason.TRIGGERED);
        if (platform == null) return InteractionResult.FAIL;

        Vec3 spawnPos = Vec3.atBottomCenterOf(pos).add(0, 0.5, 0);
        platform.setPos(spawnPos);
        platform.setIsCarrier(true);
        serverLevel.addFreshEntity(platform);

        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}