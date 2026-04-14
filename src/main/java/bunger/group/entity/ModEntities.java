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
                    .dimensions(EntityDimensions.fixed(0.6f, 0.85f)) // natural model size
                    .build()
    );
    public static final EntityType<GodEntity> GOD = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "god"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, GodEntity::new)
                    .dimensions(EntityDimensions.fixed(3.0f, 6.0f)) // wider and taller
                    .build()
    );
    public static final EntityType<SquirrelBearEntity> SQUIRREL_BEAR = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "squirrel_bear"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, SquirrelBearEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 0.85f)) // wider and taller
                    .build()
    );

    public static void register() {}
}
