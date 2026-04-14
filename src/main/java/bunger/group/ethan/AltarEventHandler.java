package bunger.group.ethan;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import bunger.group.MutuallyAssuredDestruction;

public class AltarEventHandler {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) {
                BlockPos below = player.blockPosition().below();
                if (player.level().getBlockState(below).getBlock() 
                        instanceof AltarBlock) {
                    spawnBoss((ServerLevel) player.level(), below);
                }
            }
            return true;
        });
    }


    // TODO: Add Voremoth, Excommunicado Imortalis
    private static void spawnBoss(ServerLevel level, BlockPos altarPos) {
        double spawnX = altarPos.getX() + 0.5;
        double spawnY = altarPos.getY() + 20;
        double spawnZ = altarPos.getZ() + 0.5;

        VoremothEntity boss = ModEntityTypes.VOREMOTH.create(
            level, 
            EntitySpawnReason.TRIGGERED
        );

        if (boss != null) {
            for (int i = 20; i < 40; i++) {
                BlockPos check = altarPos.above(i);
                if (level.getBlockState(check).isAir() && 
                    level.getBlockState(check.above()).isAir()) {
                    spawnY = check.getY();
                    break;
                }
            }
            
            // set position before adding to world
            boss.setPos(spawnX, spawnY, spawnZ);
            level.addFreshEntity(boss);
        }
    }
}