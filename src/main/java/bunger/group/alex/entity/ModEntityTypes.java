package bunger.group.alex.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.goblin.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Predicate;

public class ModEntityTypes {

    public static final EntityType<WraithEntity> WRAITH = register(
            "wraith",
            EntityType.Builder.<WraithEntity>of(WraithEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.6f)
    );

    public static final EntityType<GoblinGruntEntity> GOBLIN_GRUNT = register(
            "goblin_grunt",
            EntityType.Builder.<GoblinGruntEntity>of(GoblinGruntEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.5f)
    );

    public static final EntityType<GoblinMageEntity> GOBLIN_MAGE = register(
            "goblin_mage",
            EntityType.Builder.<GoblinMageEntity>of(GoblinMageEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.5f)
    );

    public static final EntityType<GoblinRangerEntity> GOBLIN_RANGER = register(
            "goblin_ranger",
            EntityType.Builder.<GoblinRangerEntity>of(GoblinRangerEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.5f)
    );

    public static final EntityType<GoblinChiefEntity> GOBLIN_CHIEF = register(
            "goblin_chief",
            EntityType.Builder.<GoblinChiefEntity>of(GoblinChiefEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.5f)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOBLIN_GRUNT,  GoblinGruntEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(GOBLIN_MAGE,   GoblinMageEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(GOBLIN_RANGER, GoblinRangerEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(GOBLIN_CHIEF,  GoblinChiefEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(WRAITH,        WraithEntity.createAttributes().build());
        registerSpawns();
    }

    public static void registerSpawns() {

        final Predicate<BiomeSelectionContext> LAND_OVERWORLD =
                BiomeSelectors.foundInOverworld()
                        .and(BiomeSelectors.tag(BiomeTags.IS_OCEAN).negate())
                        .and(BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN).negate())
                        .and(BiomeSelectors.tag(BiomeTags.IS_BEACH).negate())
                        .and(BiomeSelectors.tag(BiomeTags.IS_RIVER).negate());


        BiomeModifications.addSpawn(
                LAND_OVERWORLD,
                MobCategory.MONSTER,
                ModEntityTypes.WRAITH,
                6, 1, 1
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS),
                MobCategory.MONSTER,
                ModEntityTypes.GOBLIN_GRUNT,
                10, 1, 2
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS),
                MobCategory.MONSTER,
                ModEntityTypes.GOBLIN_RANGER,
                10, 1, 2
        );

        BiomeModifications.addSpawn(
                LAND_OVERWORLD,
                MobCategory.MONSTER,
                ModEntityTypes.GOBLIN_CHIEF,
                1, 1, 1
        );
    }
}