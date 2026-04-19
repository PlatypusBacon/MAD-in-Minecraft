package bunger.group.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import bunger.group.client.alex.Bunger1;
import bunger.group.client.bryan.Bunger2;
import bunger.group.client.csmit863.Bunger3;
import bunger.group.client.ethan.Bunger4;
import bunger.group.client.ethan.ModEntityModelLayers;
import bunger.group.client.ethan.ProphetEntityRenderer;
import bunger.group.client.ethan.VoremothEntityRenderer;
import bunger.group.client.tyler.Bunger5;
import bunger.group.ethan.ModEntityTypes;
import bunger.group.ethan.VoremothEntity;

public class MutuallyAssuredDestructionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModEntityModelLayers.registerModelLayers();
		EntityRenderers.register(ModEntityTypes.PROPHET, ProphetEntityRenderer::new);
		EntityRenderers.register(ModEntityTypes.VOREMOTH, VoremothEntityRenderer::new);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.level == null) return;
			
			for (Entity entity : client.level.entitiesForRendering()) {
				if (!(entity instanceof VoremothEntity voremoth)) continue;
				
				int targetId = voremoth.getLaserTargetId();
				if (targetId == -1) continue;
				
				Entity target = client.level.getEntity(targetId);
				if (target == null) continue;
				
				Vec3 start = new Vec3(voremoth.getEyePosition().x, voremoth.getEyePosition().y - 10, voremoth.getEyePosition().z);
				Vec3 end = new Vec3(target.getEyePosition().x, target.getEyePosition().y - 2, target.getEyePosition().z);
				Vec3 direction = end.subtract(start);
				double length = direction.length();
				Vec3 step = direction.normalize().scale(0.5);
				
				int numParticles = (int)(length / 0.5);
				for (int i = 0; i < numParticles; i++) {
					Vec3 pos = start.add(step.scale(i));
					client.level.addParticle(ParticleTypes.GLOW, pos.x, pos.y, pos.z, 0, 0, 0);

				}
			}
		});
	}
}