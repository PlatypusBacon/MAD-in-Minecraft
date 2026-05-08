package bunger.group.client.tyler;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.god.GodEntityModel;
import bunger.group.client.tyler.squirrel.SquirrelEntityModel;
import bunger.group.client.tyler.squirrel_bear.SquirrelBearEntityModel;
import bunger.group.client.tyler3.deer.DeerEntityModel;
import bunger.group.client.tyler3.dude.DudeModel;
import bunger.group.client.tyler3.shopping_cart.ShoppingCartModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.object.armorstand.ArmorStandArmorModel;
import net.minecraft.client.model.object.armorstand.ArmorStandModel;
import net.minecraft.resources.Identifier;

public class ModEntityModelLayers {
    public static final ModelLayerLocation SQUIRREL = createMain("squirrel");
    public static final ModelLayerLocation SQUIRREL_BEAR = createMain("squirrel_bear");
    public static final ModelLayerLocation GOD = createMain("god");
    public static final ModelLayerLocation SQUIRREL_WIFE = createMain("squirrel_wife");
    public static final ModelLayerLocation DUDE = createMain("dude");
    public static final ModelLayerLocation SHOPPING_CART = createMain("shopping_cart");
    public static final ModelLayerLocation DEER = createMain("deer");

    private static ModelLayerLocation createMain(String name) {

        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name), "main");
    }

    public static void registerModelLayers() {
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SQUIRREL, SquirrelEntityModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SQUIRREL_BEAR, SquirrelBearEntityModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.GOD, GodEntityModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.DUDE, DudeModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.SHOPPING_CART, ShoppingCartModel::getTexturedModelData);
        ModelLayerRegistry.registerModelLayer(ModEntityModelLayers.DEER, DeerEntityModel::getTexturedModelData);


    }
}