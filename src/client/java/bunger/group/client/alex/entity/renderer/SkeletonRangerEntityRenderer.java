package bunger.group.client.alex.entity.renderer;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.entity.SkeletonRangerEntity;
import bunger.group.client.alex.entity.model.ModEntityModelLayers;
import bunger.group.client.alex.entity.model.SkeletonRangerEntityModel;
import bunger.group.client.alex.entity.state.SkeletonRangerEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class SkeletonRangerEntityRenderer extends MobRenderer<SkeletonRangerEntity, SkeletonRangerEntityRenderState, SkeletonRangerEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "textures/entity/skeleton_ranger.png");

    private final EntityRendererProvider.Context context;

    public SkeletonRangerEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonRangerEntityModel(context.bakeLayer(ModEntityModelLayers.SKELETON_RANGER)), 0.3f);
        this.context = context;
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public SkeletonRangerEntityRenderState createRenderState() {
        return new SkeletonRangerEntityRenderState();
    }

    @Override
    public void extractRenderState(SkeletonRangerEntity entity, SkeletonRangerEntityRenderState state, float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.walkAnimPos   = entity.walkAnimation.position(tickProgress);
        state.walkAnimSpeed = entity.walkAnimation.speed(tickProgress);
        state.attackTime    = entity.getAttackAnim(tickProgress);

        int ticksUsing = entity.getTicksUsingItem();
        state.bowDrawTime = ticksUsing > 0
                ? Math.min((ticksUsing + tickProgress) / 20.0F, 1.0F)
                : 0.0F;

        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, context.getItemModelResolver(), tickProgress);

        state.leftHandItemState.clear();
        state.leftHandItemStack = ItemStack.EMPTY;
    }

    @Override
    public Identifier getTextureLocation(SkeletonRangerEntityRenderState state) {
        return TEXTURE;
    }
}