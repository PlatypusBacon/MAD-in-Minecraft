package bunger.group.ethan;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
// import net.minecraft.resources.ResourceLocation;
import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.Entity;

public class ModEntityTypes {
	public static final EntityType<ProphetEntity> PROPHET = register(
			"prophet",
			EntityType.Builder.<ProphetEntity>of(ProphetEntity::new, MobCategory.MONSTER)
					.sized(0.75f, 1.75f)
	);

	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}

	public static void registerModEntityTypes() {
		MutuallyAssuredDestruction.LOGGER.info("Registering EntityTypes for " + MutuallyAssuredDestruction.MOD_ID);
		// Ensure the entity types are loaded
		var prophet = PROPHET;
	}

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(PROPHET, ProphetEntity.createCubeAttributes());
	}
}
