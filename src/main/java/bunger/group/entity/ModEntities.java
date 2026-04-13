package bunger.group.entity;

import bunger.group.MutuallyAssuredDestruction;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<SquirrelEntity> SQUIRREL = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation("mutually-assured-destruction", "squirrel"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, SquirrelEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0f, 2.5f))
                    .build()
    );
    public static final EntityType<GodEntity> GOD = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "god"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, GodEntity::new)
                    .dimensions(EntityDimensions.fixed(3.0f, 6.0f)) // wider and taller
                    .build()
    );
    public static void register() {}
}
