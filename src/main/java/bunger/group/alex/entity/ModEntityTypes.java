package bunger.group.alex.entity;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

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

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(GOBLIN_GRUNT, GoblinGruntEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(GOBLIN_MAGE, GoblinMageEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(WRAITH, WraithEntity.createAttributes().build());
        registerSpawns();
    }

    public static void registerSpawns() {
        // Modify spawning
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                ModEntityTypes.WRAITH,
                8, 1, 1
        );

        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                MobCategory.MONSTER,
                ModEntityTypes.GOBLIN_GRUNT,
                5, 4, 10
        );
    }
}