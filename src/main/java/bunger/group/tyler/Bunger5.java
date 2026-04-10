package bunger.group.tyler;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;

public class Bunger5 {

    public static final ResourceKey<Biome> MY_BIOME = register("bunger_biome");

    private static ResourceKey<Biome> register(final String name) {
        return ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace(name));
    }

    public static Biome createBiome() {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(2.0f)          // very hot
                .downfall(0.0f)             // no rain

                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x3F76E4)
                        .build())

                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)

                .build();
    }
}