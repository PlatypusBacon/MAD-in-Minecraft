package bunger.group.alex.spell;

import bunger.group.alex.item.ModItems;
import bunger.group.alex.item.spell.SpellTypes;

import java.util.List;

public class SpellRegistry {

    public static final List<SpellDefinition> ALL_SPELLS = List.of(

            // EARTH
            new SpellDefinition("Impale", SpellTypes.EARTH,15, ModItems.IMPALE),
            new SpellDefinition("Forest of Spikes", SpellTypes.EARTH,75, ModItems.FOREST_OF_SPIKES),

            // FIRE
            new SpellDefinition("Ignition", SpellTypes.FIRE,15, ModItems.IGNITION),

            // ICE
            new SpellDefinition("Ice Shield",SpellTypes.ICE,10, ModItems.ICE_SHIELD),

            // LIGHTNING
            new SpellDefinition("Zap", SpellTypes.LIGHTNING, 10, ModItems.ZAP),
            new SpellDefinition("Channel Storm", SpellTypes.LIGHTNING, 50, ModItems.CHANNEL_STORM),

            // WATER
            new SpellDefinition("Summon Water", SpellTypes.WATER, 5, ModItems.SUMMON_WATER),
            new SpellDefinition("Invoke Rain", SpellTypes.WATER, 30, ModItems.INVOKE_RAIN),

            // AGARTHA
            new SpellDefinition("Agarthan Thunder", SpellTypes.AGARTHA,100, ModItems.AGARTHAN_THUNDER),
            new SpellDefinition("Agarthan Ice Dome",SpellTypes.AGARTHA,100, ModItems.AGARTHAN_ICE_DOME)
    );

    public static List<SpellDefinition> getSpellsForType(SpellTypes type) {
        return ALL_SPELLS.stream()
                .filter(s -> s.type() == type)
                .toList();
    }
}