package bunger.group.alex.item;

import net.minecraft.world.item.Item;

public class BlankScroll extends Item {

    SpellTypes TYPE;

    public BlankScroll(Properties properties, SpellTypes type) {
        super(properties);
        TYPE = type;

    }

    public SpellTypes getType() {
        return this.TYPE;
    }
}
