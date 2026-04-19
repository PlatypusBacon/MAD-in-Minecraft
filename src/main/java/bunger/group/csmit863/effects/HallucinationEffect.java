package bunger.group.csmit863.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HallucinationEffect extends MobEffect {
    public HallucinationEffect() {
        // category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
        // color: int - Color is the color assigned to the effect (in RGB)
        super(MobEffectCategory.BENEFICIAL, 0xe9b8b3);
    }

    // Called every tick to check if the effect can be applied or not
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the effect every tick
        return true;
    }

    // Called when the effect is applied.
    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {

            // low probability each tick (adjust)
            if (player.getRandom().nextFloat() < 0.004f * amplifier) {
                // pick a random sound
                SoundEvent sound;
                int r = player.getRandom().nextInt(3);
                switch (r) {
                    case 0 -> sound = SoundEvents.ENDERMAN_TELEPORT;
                    case 1 -> sound = SoundEvents.CREEPER_PRIMED; // hiss
                    default -> sound = SoundEvents.PLAYER_BREATH; // substitute for footsteps if needed
                }
                // play at player's position with varied pitch/volume
                float volume = 0.7f + player.getRandom().nextFloat() * 0.6f;
                float pitch = 0.8f + player.getRandom().nextFloat() * 0.6f;
                player.playSound(sound, volume, pitch);
            }

        }
        return super.applyEffectTick(level, entity, amplifier);
    }
}