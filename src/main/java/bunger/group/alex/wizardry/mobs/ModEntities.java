package bunger.group.alex.wizardry.mobs;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityDimensions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModEntities {

    public static final EntityType<Wizard> WIZARD = Registry.register(
            Registry.ENTITY_TYPE,
            new ResourceLocation("mutually-assured-destruction", "wizard"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, Wizard::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(
                WIZARD,
                Wizard.createAttributes()
        );
    }
}