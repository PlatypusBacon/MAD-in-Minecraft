package bunger.group.tyler;

import bunger.group.tyler.Bunger5;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.resources.Identifier;

public class BungerBiomeEntrypoint {

    public static void init() {
        addBiomeToOverworld();
    }

    private static void addBiomeToOverworld() {

        BiomeModifications.create(new Identifier("mutually-assured-destruction", "add_bunger_biome"))
                .add(
                        ModificationPhase.ADDITIONS,
                        BiomeSelectors.foundInOverworld(),
                        context -> {
                            context.getEffects(); // forces load (keeps context valid)
                        }
                );
    }
}