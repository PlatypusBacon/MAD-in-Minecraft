package bunger.group.tyler;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class BungerBiomeProvider extends FabricDynamicRegistryProvider {

    public BungerBiomeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        entries.add(Bunger5.MY_BIOME, Bunger5.createBiome());
    }

    @Override
    public String getName() {
        return "Bunger Biomes";
    }
}