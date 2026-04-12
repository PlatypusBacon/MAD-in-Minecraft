package bunger.group.ethan;

import java.util.UUID;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class RedRainHandler {

    public static void register() {
        	ServerTickEvents.END_SERVER_TICK.register(server -> {
				for (ServerLevel level : server.getAllLevels()) {
					for (ServerPlayer player : level.players()) {
						UUID uuid = player.getUUID();
						boolean hasRedDarkness = player.hasEffect(
							BuiltInRegistries.MOB_EFFECT.wrapAsHolder(RedDarknessEffect.RED_DARKNESS)
						);


						// particles
						if (MutuallyAssuredDestruction.RED_RAIN_PLAYERS.containsKey(uuid)) {
							// System.out.println("USERTEST: Tick: " + player.level().getGameTime());
							long endTime = MutuallyAssuredDestruction.RED_RAIN_PLAYERS.get(uuid);
							
							if (player.level().getGameTime() >= endTime) {
								MutuallyAssuredDestruction.RED_RAIN_PLAYERS.remove(uuid);
							} else {
								if (player.level().canSeeSky(player.blockPosition())) {
									if (player.tickCount % 60 != 0) {										
											for (int i = 0; i < 20; i++) {
												double offsetX = (Math.random() - 0.5) * 20;
												double offsetZ = (Math.random() - 0.5) * 20;
												level.sendParticles(
													ParticleTypes.FALLING_DRIPSTONE_LAVA,
													player.getX() + offsetX,
													player.getY() + 20,
													player.getZ() + offsetZ,
													1, 0, 0, 0, 0.2
												);
											}
											// if (player.tickCount % 5 == 0) {
											// 	player.hurt(
											// 		player.damageSources().generic(),
											// 		0.5F 
											// 	);
											// }

									}
								}
							}
            			}

						// Lightning
						if (hasRedDarkness && player.tickCount % 60 == 0) {
							server.execute(new TickTask(server.getTickCount() + 1, () -> {
								LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(
									level, EntitySpawnReason.TRIGGERED
								);
								if (lightning != null) {
									lightning.move(MoverType.PLAYER, new Vec3(player.getX(), player.getY(), player.getZ()));
									lightning.setVisualOnly(false);
									lightning.setSilent(false);
									level.addFreshEntity(lightning);
								}
							}));
						}
        			}
    			}
			});
    }
    
}
