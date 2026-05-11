package bunger.group.alex.spell;

import bunger.group.alex.item.spell.SpellTypes;
import net.minecraft.world.item.Item;

public record SpellDefinition(String name, SpellTypes type, int manaCost, Item result) {}