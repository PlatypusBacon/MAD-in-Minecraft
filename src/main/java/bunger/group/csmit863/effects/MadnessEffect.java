package bunger.group.csmit863.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MadnessEffect extends MobEffect {
    public MadnessEffect() {
        super(MobEffectCategory.HARMFUL, 0x3B0A45);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        // server-side madness logic goes here later
        return super.applyEffectTick(level, entity, amplifier);
    }
}