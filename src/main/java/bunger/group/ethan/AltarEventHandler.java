package bunger.group.ethan;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;

public class AltarEventHandler {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayer player) { //TODO: this should not be prophet entity
                BlockPos below = player.blockPosition().below();
                 System.out.println("ALTARTEST: Player died, block below: " + player.level().getBlockState(below).getBlock());
                if (player.level().getBlockState(below).getBlock() instanceof AltarBlock) {
                    System.out.println("ALTARTEST: Spawning new boss!");
                    spawnBoss((ServerLevel) player.level(), below);
                }
            }
            return true;
        });
    }



    public static void spawnBoss(ServerLevel level, BlockPos altarPos) {
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
            boss.setPersistenceRequired();
            boss.setPos(spawnX, spawnY, spawnZ);
            level.addFreshEntity(boss);
            VoremothBossMechanic.startMechanic(boss, altarPos);
        }
    }
}