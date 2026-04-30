package bunger.group.tyler3.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.entity.PlatformEntity;
import bunger.group.tyler3.tools.ChunkAttachmentHelper;
import bunger.group.tyler3.tools.EntityAttachmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StickyGooItem extends Item {

    public StickyGooItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();
        BlockPos neighbour = pos.relative(face);
        BlockState fromState = level.getBlockState(pos);

        if (fromState.isAir()) return InteractionResult.FAIL;
        if (ChunkAttachmentHelper.hasAttachment(level, pos, face)) {
            return InteractionResult.FAIL;
        }

        // Standard block-to-block case
        if (!level.isClientSide()) {
            ChunkAttachmentHelper.applyAttachmentOneFace(level, pos, face);
            BlockState neighbourState = level.getBlockState(neighbour);
            if (!neighbourState.isAir()) {
                ChunkAttachmentHelper.applyAttachmentOneFace(level, neighbour, face.getOpposite());
            } else {
                ChunkAttachmentHelper.markPending(pos, face);
            }
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}