package bunger.group.mixin;

import bunger.group.tyler.event.god.StructureProtection;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerExplosion.class)
public class ExplosionMixin {

    @Shadow @Final private ServerLevel level;

    @Inject(method = "interactWithBlocks", at = @At("HEAD"))
    private void filterProtectedBlocks(List<BlockPos> targetBlocks, CallbackInfo ci) {
        targetBlocks.removeIf(pos -> StructureProtection.shouldProtectFromExplosion(level, pos));
    }
}