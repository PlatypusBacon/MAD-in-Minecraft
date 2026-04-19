package bunger.group.alex.item;

import bunger.group.alex.Mana;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellTemplate extends Item {

    int MANA_USE;
    int RANGE;
    SpellTypes TYPE;

    public SpellTemplate(Properties properties, int manaUse, int range, SpellTypes type) {
        properties.stacksTo(1);
        this.MANA_USE = manaUse;
        this.RANGE = range;
        this.TYPE = type;
        super(properties);
    }

    public void cast(Level level, LivingEntity user, ItemStack stack) {
        System.out.println("Some idiot forgot to @Override public void cast(Level level, LivingEntity user, ItemStack stack){}...");
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (Mana.get(user).getCurrentMana() >= this.MANA_USE){
            // Enough mana to use, thus use.

            Mana.get(user).useMana(this.MANA_USE);

            ItemStack stack = user.getItemInHand(hand);
            this.cast(level, user, stack);

            return InteractionResult.SUCCESS;
        }

        user.sendOverlayMessage(Component.literal("Not enough mana!"));

        return InteractionResult.PASS;
    }

}

enum SpellTypes {
    LIGHTNING, ICE, FIRE, STAFF
}