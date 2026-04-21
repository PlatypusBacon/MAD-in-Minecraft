package bunger.group.alex.spell;

import bunger.group.alex.item.ModItems;
import bunger.group.alex.item.SpellTypes;

import java.util.List;

public class SpellRegistry {

    public static final List<SpellDefinition> ALL_SPELLS = List.of(
            // ICE
            new SpellDefinition("Ice Shield",SpellTypes.ICE,10, ModItems.ICE_SHIELD),
            new SpellDefinition("Agarthan Ice Dome",SpellTypes.ICE,75, ModItems.AGARTHAN_ICE_DOME),

            // LIGHTNING
            new SpellDefinition("Zap", SpellTypes.LIGHTNING, 10, ModItems.ZAP),
            new SpellDefinition("Agarthan Thunder", SpellTypes.LIGHTNING,75, ModItems.AGARTHAN_THUNDER),


            // FIRE
            new SpellDefinition("Ignition", SpellTypes.FIRE,15, ModItems.IGNITION),

            // STAFF
            new SpellDefinition("Staff of Teleportation", SpellTypes.STAFF, 30, ModItems.STAFF_OF_TELEPORTATION)
    );

    public static List<SpellDefinition> getSpellsForType(SpellTypes type) {
        return ALL_SPELLS.stream()
                .filter(s -> s.type() == type)
                .toList();
    }
}