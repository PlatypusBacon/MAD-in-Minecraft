package bunger.group.tyler3;

import bunger.group.tyler3.entity.DudeEntity;
import bunger.group.tyler3.entity.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Predicate;

public class RegisterSpawns {
    public static void register() {
        Predicate<BiomeSelectionContext> selector = BiomeSelectors.foundInOverworld()
                .or(BiomeSelectors.foundInTheNether());

        BiomeModifications.addSpawn(selector, MobCategory.MONSTER, ModEntities.DUDE, 1, 1, 1);
              /*SpawnPlacements.register(
                ModEntities.DUDE,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules
        );*/

        SpawnPlacements.register(
                ModEntities.DUDE,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, spawnReason, pos, random) ->
                        random.nextFloat() < 0.1f
                                && Mob.checkMobSpawnRules(type, level, spawnReason, pos, random)
        );


        Predicate<BiomeSelectionContext> deerSelector = BiomeSelectors.foundInOverworld();
        BiomeModifications.addSpawn(deerSelector, MobCategory.CREATURE, ModEntities.DEER, 10, 1, 4);
        SpawnPlacements.register(
                ModEntities.DEER,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );
    }
}
