package bunger.group.tyler3.entity;

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
    public static final EntityType<DudeEntity> DUDE = register(
            "dude",
            EntityType.Builder.<DudeEntity>of(DudeEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.5f)
    );
    public static final EntityType<ShoppingCartEntity> SHOPPING_CART = register("shopping_cart",
            EntityType.Builder.<ShoppingCartEntity>of(ShoppingCartEntity::new, EntityType.MINECART.getCategory())
                    .sized(1.5f, 0.9f)
    );
    public static final EntityType<DeerEntity> DEER = register(
            "deer",
            EntityType.Builder.of(DeerEntity::new, MobCategory.CREATURE)

    );
    public static final EntityType<RockProjectileEntity> ROCK_PROJECTILE = register(
            "rock_projectile",
            EntityType.Builder.<RockProjectileEntity>of(RockProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }
    public static void registerModEntityTypes() {
        MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
        // Ensure the entity types are loaded
        var squirrel = DUDE;
        var deer = DEER;
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(DUDE, DudeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DEER, DeerEntity.createAttributes());
    }
}
