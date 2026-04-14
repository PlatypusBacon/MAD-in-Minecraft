package bunger.group.client.alex.wizardry;

import bunger.group.alex.wizardry.mobs.ModEntities;
import bunger.group.alex.wizardry.mobs.Wizard;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ModClient {

    public static void init() {

        EntityRendererRegistry.register(ModEntities.WIZARD,
                (EntityRendererProvider.Context context) ->
                        new MobRenderer<Wizard, HumanoidModel<Wizard>>(
                                context,
                                new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                                0.5f
                        ) {

                            @Override
                            public ResourceLocation getTextureLocation(Wizard entity) {
                                return new ResourceLocation(
                                        "minecraft",
                                        "textures/entity/zombie/zombie.png"
                                );
                            }
                        }
        );
    }
}