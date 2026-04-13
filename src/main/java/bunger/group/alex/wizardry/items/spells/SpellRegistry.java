package bunger.group.alex.wizardry.items.spells;

import bunger.group.alex.wizardry.items.spells.*;

public class SpellRegistry {

    public static void register() {
        DefaultSpells.register();
        FireSpells.register();
        LightningSpells.register();
        WaterSpells.register();
        IceSpells.register();
    }
}
