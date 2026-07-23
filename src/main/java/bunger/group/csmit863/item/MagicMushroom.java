package bunger.group.csmit863.item;
import bunger.group.csmit863.block.ModBlocks;
import bunger.group.csmit863.effects.HallucinationEffect;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagicMushroom extends BlockItem {
    private static final int BASE_DURATION = 20 * 30;   // 600 ticks
    private static final int MAX_DURATION  = 20 * 60 * 10; // 12000 ticks
    private static final int BASE_AMPLIFIER = 0;
    private static final int MAX_AMPLIFIER = 4;

    public MagicMushroom(Properties properties) { super(ModBlocks.MAGIC_MUSHROOM_BLOCK, properties); }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide() && user instanceof Player player) {
            MobEffectInstance current = player.getEffect(ModItems.HALLUCINATION_EFFECT);
            int newAmp = BASE_AMPLIFIER;
            int newDuration = BASE_DURATION;
            if (current != null) {
                newAmp = Math.min(MAX_AMPLIFIER, current.getAmplifier() + 1);
                newDuration = Math.min(MAX_DURATION, current.getDuration() + BASE_DURATION);
            }
            player.addEffect(new MobEffectInstance(ModItems.HALLUCINATION_EFFECT, newDuration, newAmp));
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, newDuration, newAmp));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, newDuration, newAmp));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, newDuration, newAmp));
        }
        return super.finishUsingItem(stack, world, user);
    }
}