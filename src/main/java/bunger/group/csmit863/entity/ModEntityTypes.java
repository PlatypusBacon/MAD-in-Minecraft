package bunger.group.csmit863.entity;

import bunger.group.MutuallyAssuredDestruction;
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
    public static final EntityType<ShroomjakEntity> SHROOMJAK = register(
            "shroomjak",
            EntityType.Builder.<ShroomjakEntity>of(ShroomjakEntity::new, MobCategory.CREATURE)
                    .sized(0.54f, 1.53f) // width, height
    );

    public static final EntityType<OverseerEntity> OVERSEER = register(
            "overseer",
            EntityType.Builder.<OverseerEntity>of(OverseerEntity::new, MobCategory.MONSTER)
                    .sized(4.0f, 4.0f)
                    .noSave()
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void registerModEntityTypes() {
        MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(SHROOMJAK, ShroomjakEntity.createCubeAttributes());
        FabricDefaultAttributeRegistry.register(OVERSEER, OverseerEntity.createAttributes());
    }
}