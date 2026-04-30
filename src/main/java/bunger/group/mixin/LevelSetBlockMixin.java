package bunger.group.mixin;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.tools.ChunkAttachmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Level.class)
public abstract class LevelSetBlockMixin {

    private static final ThreadLocal<Boolean> APPLYING_ATTACHMENT = ThreadLocal.withInitial(() -> false);

    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            at = @At("RETURN")
    )
    private void onSetBlock(BlockPos pos, BlockState newState, int flags,
                            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (APPLYING_ATTACHMENT.get()) return;

        Level level = (Level)(Object)this;
        if (level.isClientSide()) return;
        if (newState.is(Blocks.MOVING_PISTON) || newState.is(Blocks.PISTON_HEAD)) return;

        // ONLY handle removal — placement reciprocals are handled in useOn and transferAttachments
        if (newState.isAir()) {
            boolean pistonDriven = false;
            for (Direction d : Direction.values()) {
                BlockState neighbour = level.getBlockState(pos.relative(d));
                if (neighbour.is(Blocks.MOVING_PISTON) || neighbour.is(Blocks.PISTON)
                        || neighbour.is(Blocks.STICKY_PISTON)) {
                    pistonDriven = true;
                    break;
                }
            }
            if (pistonDriven) return;

            APPLYING_ATTACHMENT.set(true);
            try {
                for (Direction face : Direction.values()) {
                    if (ChunkAttachmentHelper.hasAttachment(level, pos, face)) {
                        ChunkAttachmentHelper.removeAttachmentOneFace(
                                level, pos.relative(face), face.getOpposite());
                    }
                }
                ChunkAttachmentHelper.clearAllFaces(level, pos);
            } finally {
                APPLYING_ATTACHMENT.set(false);
            }
        } else {
        APPLYING_ATTACHMENT.set(true);
        try {
            for (Direction face : Direction.values()) {
                BlockPos neighbour = pos.relative(face);
                Direction reciprocal = face.getOpposite();
                // Only complete if explicitly marked as pending toward us
                if (ChunkAttachmentHelper.isPending(neighbour, reciprocal)) {
                    MutuallyAssuredDestruction.LOGGER.info("[LevelSetBlock] completing pending: pos={} face={} from neighbour={}",
                            pos, face, neighbour);
                    ChunkAttachmentHelper.applyAttachmentOneFace(level, pos, face);
                    ChunkAttachmentHelper.clearPending(neighbour, reciprocal);
                }
            }
        } finally {
            APPLYING_ATTACHMENT.set(false);
        }
    }
        // Placement: do nothing — reciprocals are written explicitly in useOn
    }
}