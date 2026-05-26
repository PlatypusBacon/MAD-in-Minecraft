package bunger.group.alex.effect;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {

    public static final Holder<MobEffect> MANA_BOOST =
            Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_boost"),
                    new ManaBoostEffect());

    public static void register() {}
}
