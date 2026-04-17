package bunger.group.ethan;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> BEACON_DAMAGE = ResourceKey.create(
        Registries.DAMAGE_TYPE,
        Identifier.fromNamespaceAndPath("mutually-assured-destruction", "beacon_damage")
    );
}
