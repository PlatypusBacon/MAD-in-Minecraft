package bunger.group.client.ethan;

import bunger.group.ethan.RedDarknessEffect;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.MobEffectFogEnvironment;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class RedDarknessFogEnvironment extends MobEffectFogEnvironment {

    @Override
    public Holder<MobEffect> getMobEffect() {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(RedDarknessEffect.RED_DARKNESS);
        //return Holder.direct(RedDarknessEffect.RED_DARKNESS);
    }

    @Override
    public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
        //System.out.println("USERTEST: RedDarkness setupFog called");
        if (camera.entity() instanceof LivingEntity livingEntity) {
            MobEffectInstance effect = livingEntity.getEffect(this.getMobEffect());
            if (effect != null) {
                float distance = Mth.lerp(
                    effect.getBlendFactor(livingEntity, deltaTracker.getGameTimeDeltaPartialTick(false)),
                    renderDistance,
                    8.0F
                );
                fog.environmentalStart = distance * 0.5F;
                fog.environmentalEnd = distance;
                fog.skyEnd = distance;
                fog.cloudEnd = distance;
                fog.color.set(0.15F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    @Override
    public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
        MobEffectInstance instance = entity.getEffect(this.getMobEffect());
        return instance != null ? Math.max(instance.getBlendFactor(entity, partialTickTime), darkness) : darkness;
    }
}
