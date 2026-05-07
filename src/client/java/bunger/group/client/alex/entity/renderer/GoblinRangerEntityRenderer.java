package bunger.group.client.alex.entity.renderer;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.goblin.GoblinRangerEntity;
import bunger.group.client.alex.entity.model.GoblinRangerEntityModel;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.state.GoblinRangerEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GoblinRangerEntityRenderer extends MobRenderer<GoblinRangerEntity, GoblinRangerEntityRenderState, GoblinRangerEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/goblin_ranger.png");

    private final EntityRendererProvider.Context context;

    public GoblinRangerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GoblinRangerEntityModel(context.bakeLayer(ModEntityModelLayers.GOBLIN_RANGER)), 0.3f);
        this.context = context;
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public GoblinRangerEntityRenderState createRenderState() {
        return new GoblinRangerEntityRenderState();
    }

    @Override
    public void extractRenderState(GoblinRangerEntity entity, GoblinRangerEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.walkAnimPos  = entity.walkAnimation.position(tickProgress);
        state.walkAnimSpeed = entity.walkAnimation.speed(tickProgress);
        state.attackTime   = entity.getAttackAnim(tickProgress);

        // Bow draw: normalize ticks using item against full draw time (20 ticks)
        int ticksUsing = entity.getTicksUsingItem();
        state.bowDrawTime = ticksUsing > 0
                ? Math.min((ticksUsing + tickProgress) / 20.0F, 1.0F)
                : 0.0F;

        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, context.getItemModelResolver(), tickProgress);

        if (entity.getMainHandItem().is(Items.BOW)) {
            state.leftHandItemState.clear();
            state.leftHandItemStack = ItemStack.EMPTY;
        }
    }

    @Override
    public Identifier getTextureLocation(GoblinRangerEntityRenderState state) {
        return TEXTURE;
    }
}