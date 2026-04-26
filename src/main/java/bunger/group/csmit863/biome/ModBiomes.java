package bunger.group.csmit863.biome;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;

public class ModBiomes {
    public static final ResourceKey<Biome> SHROOM_SHIRE = ResourceKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "shroom_shire")
    );

    public static final ResourceKey<Biome> MAD_REALM_BIOME = ResourceKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mad_realm")
    );

    public static final ResourceKey<DimensionType> MAD_REALM_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mad_realm")
    );

    public static final ResourceKey<Level> MAD_REALM = ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "mad_realm")
    );

    public static void initialise() {
        MutuallyAssuredDestruction.LOGGER.info("Registering " + MutuallyAssuredDestruction.MOD_ID + " Biomes");
    }
}