package bunger.group.alex.effect;

import bunger.group.alex.Mana;
import bunger.group.alex.ManaPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ManaBoostEffect extends MobEffect {

    public static int BOOST = 30;

    protected ManaBoostEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x8557FF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = Math.max(1, 20 / (amplifier + 1));
        return duration % interval == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        // Regen mana
        if (entity instanceof ServerPlayer player) {
            Mana.ManaData mana = Mana.get(player);
            if (mana.getCurrentMana() < mana.getMaxMana()) {
                mana.incrementCurrentMana();
                ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
            }
        }
        return true;
    }

}
