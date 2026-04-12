package bunger.group.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<SquirrelEntity> SQUIRREL = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation("mutully-assured-destruction", "squirrel"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, SquirrelEntity::new)
                    .dimensions(EntityDimensions.fixed(2.0f, 2.5f))
                    .build()
    );

    public static void register() {}
}
