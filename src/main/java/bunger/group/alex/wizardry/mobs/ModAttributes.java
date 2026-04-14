package bunger.group.alex.wizardry.mobs;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModAttributes {

    public static void register() {
        FabricDefaultAttributeRegistry.register(
                ModEntities.WIZARD,
                Wizard.createAttributes()
        );
    }
}