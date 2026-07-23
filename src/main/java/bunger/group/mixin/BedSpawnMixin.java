package bunger.group.mixin;

import bunger.group.tyler.data.StructureEventData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = BedBlock.class, remap = false)
public class BedSpawnMixin {

    @Inject(
            method = "useWithoutItem",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onBedUse(BlockState state, Level level, BlockPos pos,
                          Player player, BlockHitResult hitResult,
                          CallbackInfoReturnable<InteractionResult> cir) {

        if (level.isClientSide()) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ServerLevel serverLevel = (ServerLevel) level;
        StructureEventData data = StructureEventData.get(serverLevel);

        if (!data.isSpawnpointLocked()) return;
        if (!data.isEventPlayer(serverPlayer.getUUID())) return;

        BlockPos structureBed = data.getBedPos();
        boolean isStructureBed = pos.equals(structureBed)
                || pos.closerThan(structureBed, 2.0);

        if (!isStructureBed) {
            serverPlayer.sendSystemMessage(
                    Component.literal("§4§lYou should Go sleep at your home."));
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}