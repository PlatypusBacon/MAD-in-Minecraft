package bunger.group.tyler.entity;

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

public class ModEntities {
    public static final EntityType<SquirrelEntity> SQUIRREL = register(
            "squirrel",
            EntityType.Builder.<SquirrelEntity>of(SquirrelEntity::new, MobCategory.AMBIENT)
                    .sized(1.5f, 2.0f)
    );
    public static final EntityType<SquirrelBearEntity> SQUIRREL_BEAR = register(
            "squirrel_bear",
            EntityType.Builder.<SquirrelBearEntity>of(SquirrelBearEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.5f)
    );
    public static final EntityType<GodEntity> GOD = register(
            "god",
            EntityType.Builder.<GodEntity>of(GodEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.5f)
    );
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }
    public static void registerModEntityTypes() {
        MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
        // Ensure the entity types are loaded
        var squirrel = SQUIRREL;
        var squirrel_bear = SQUIRREL_BEAR;
        var god = GOD;
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(SQUIRREL, SquirrelEntity.createCubeAttributes());
        FabricDefaultAttributeRegistry.register(SQUIRREL_BEAR, SquirrelBearEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GOD, GodEntity.createAttributes());
    }
}
