package bunger.group.alex.item.potion;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.effect.ModEffects;
import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class ModPotions {

    public static final Holder<Potion> MANA_BOOST_POTION_I =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_boost_1"),
                    new Potion("mana_boost_1",
                            new MobEffectInstance(
                                    ModEffects.MANA_BOOST,
                                    1800,
                                    0)));

    public static final Holder<Potion> MANA_BOOST_POTION_II =
            Registry.registerForHolder(
                    BuiltInRegistries.POTION,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mana_boost_2"),
                    new Potion("mana_boost_2",
                            new MobEffectInstance(
                                    ModEffects.MANA_BOOST,
                                    1800,
                                    1)));

    public static void register() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, ModItems.PURE_MANA, MANA_BOOST_POTION_I);
            builder.addMix(MANA_BOOST_POTION_I, Items.GLOWSTONE_DUST, MANA_BOOST_POTION_II);
        });
    }

}