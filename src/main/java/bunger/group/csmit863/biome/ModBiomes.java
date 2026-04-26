package bunger.group.csmit863.biome;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> SHROOM_SHIRE = ResourceKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "shroom_shire")
    );

    public static void initialise() {
        MutuallyAssuredDestruction.LOGGER.info("Registering " + MutuallyAssuredDestruction.MOD_ID + " Biomes");
    }
}