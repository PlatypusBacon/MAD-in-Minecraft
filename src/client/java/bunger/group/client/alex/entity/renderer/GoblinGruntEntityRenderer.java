package bunger.group.client.alex.entity.renderer;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.GoblinGruntEntity;
import bunger.group.client.alex.entity.model.GoblinGruntEntityModel;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.state.GoblinGruntEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GoblinGruntEntityRenderer extends MobRenderer<GoblinGruntEntity, GoblinGruntEntityRenderState, GoblinGruntEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/goblin_grunt.png");

    private final EntityRendererProvider.Context context;

    public GoblinGruntEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GoblinGruntEntityModel(context.bakeLayer(ModEntityModelLayers.GOBLIN_GRUNT)), 0.3f);
        this.context = context;
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public GoblinGruntEntityRenderState createRenderState() {
        return new GoblinGruntEntityRenderState();
    }

    @Override
    public void extractRenderState(GoblinGruntEntity entity, GoblinGruntEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.walkAnimPos = entity.walkAnimation.position(tickProgress);
        state.walkAnimSpeed = entity.walkAnimation.speed(tickProgress);
        state.attackTime = entity.getAttackAnim(tickProgress); // add this
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, context.getItemModelResolver(), tickProgress);

        if (entity.getMainHandItem().is(Items.BOW)) {
            state.leftHandItemState.clear();
            state.leftHandItemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public Identifier getTextureLocation(GoblinGruntEntityRenderState state) {
        return TEXTURE;
    }
}