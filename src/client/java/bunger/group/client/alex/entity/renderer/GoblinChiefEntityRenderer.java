package bunger.group.client.alex.entity.renderer;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.goblin.GoblinChiefEntity;
import bunger.group.client.alex.entity.model.GoblinChiefEntityModel;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.state.GoblinChiefEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GoblinChiefEntityRenderer extends MobRenderer<GoblinChiefEntity, GoblinChiefEntityRenderState, GoblinChiefEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/goblin_chief.png");

    private final EntityRendererProvider.Context context;

    public GoblinChiefEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GoblinChiefEntityModel(context.bakeLayer(ModEntityModelLayers.GOBLIN_CHIEF)), 0.3f);
        this.context = context;
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public GoblinChiefEntityRenderState createRenderState() {
        return new GoblinChiefEntityRenderState();
    }

    @Override
    public void extractRenderState(GoblinChiefEntity entity, GoblinChiefEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.walkAnimPos = entity.walkAnimation.position(tickProgress);
        state.walkAnimSpeed = entity.walkAnimation.speed(tickProgress);
        state.attackTime = entity.getAttackAnim(tickProgress);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, context.getItemModelResolver(), tickProgress);

        if (entity.getMainHandItem().is(Items.BOW)) {
            state.leftHandItemState.clear();
            state.leftHandItemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public Identifier getTextureLocation(GoblinChiefEntityRenderState state) {
        return TEXTURE;
    }
}