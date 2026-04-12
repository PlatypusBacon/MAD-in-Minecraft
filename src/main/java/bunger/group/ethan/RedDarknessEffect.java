package bunger.group.ethan;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class RedDarknessEffect extends MobEffect {
    public RedDarknessEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    public static final MobEffect RED_DARKNESS = new RedDarknessEffect();
}
