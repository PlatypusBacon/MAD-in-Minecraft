package bunger.group.client.tyler3.shopping_cart;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.client.tyler.ModEntityModelLayers;
import bunger.group.tyler3.entity.ShoppingCartEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.object.cart.MinecartModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.minecart.MinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.entity.vehicle.minecart.OldMinecartBehavior;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ShoppingCartRenderer
        extends EntityRenderer<ShoppingCartEntity, ShoppingCartRenderState> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            MutuallyAssuredDestruction.MOD_ID, "textures/entity/shopping_cart.png");

    // MinecartModel is the base cart model that AbstractMinecartRenderer uses.
    // We bake our own layer into it so it uses our geometry.
    protected final ShoppingCartModel model;

    public ShoppingCartRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.7F;
        this.model = new ShoppingCartModel(context.bakeLayer(ModEntityModelLayers.SHOPPING_CART));
    }

    @Override
    public ShoppingCartRenderState createRenderState() {
        return new ShoppingCartRenderState();
    }

    @Override
    public void extractRenderState(ShoppingCartEntity entity,
                                   ShoppingCartRenderState state,
                                   float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        MinecartBehavior behavior = entity.getBehavior();
        if (behavior instanceof NewMinecartBehavior newBehavior) {
            if (newBehavior.cartHasPosRotLerp()) {
                state.renderPos = newBehavior.getCartLerpPosition(partialTicks);
                state.xRot = newBehavior.getCartLerpXRot(partialTicks);
                state.yRot = newBehavior.getCartLerpYRot(partialTicks);
            } else {
                state.renderPos = null;
                state.xRot = entity.getXRot();
                state.yRot = entity.getYRot();
            }
            state.isNewRender = true;
        } else if (behavior instanceof OldMinecartBehavior oldBehavior) {
            state.xRot = entity.getXRot(partialTicks);
            state.yRot = entity.getYRot(partialTicks);
            double ex = state.x, ey = state.y, ez = state.z;
            Vec3 pos = oldBehavior.getPos(ex, ey, ez);
            if (pos != null) {
                state.posOnRail = pos;
                Vec3 p0 = oldBehavior.getPosOffs(ex, ey, ez, 0.3);
                Vec3 p1 = oldBehavior.getPosOffs(ex, ey, ez, -0.3);
                state.frontPos = Objects.requireNonNullElse(p0, pos);
                state.backPos  = Objects.requireNonNullElse(p1, pos);
            } else {
                state.posOnRail = state.frontPos = state.backPos = null;
            }
            state.isNewRender = false;
        }

        // Jitter seed (same formula as AbstractMinecartRenderer)
        long seed = (long) entity.getId() * 493286711L;
        state.offsetSeed = seed * seed * 4392167121L + seed * 98761L;

        state.hurtTime   = (float) entity.getHurtTime() - partialTicks;
        state.hurtDir    = entity.getHurtDir();
        state.damageTime = Math.max(entity.getDamage() - partialTicks, 0.0F);
        state.displayOffset = entity.getDisplayOffset();
    }

    @Override
    public void submit(ShoppingCartRenderState state,
                       PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       CameraRenderState camera) {
        // Handles leashes and nametags
        super.submit(state, poseStack, submitNodeCollector, camera);

        poseStack.pushPose();

        // Per-entity jitter (anti-z-fighting, matches vanilla)
        long seed = state.offsetSeed;
        float offX = (((float)(seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float offY = (((float)(seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float offZ = (((float)(seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        poseStack.translate(offX, offY, offZ);

        // Apply position/rotation transforms depending on behavior mode
        if (state.isNewRender) {
            poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-state.xRot));
            poseStack.translate(0.0F, 0.375F, 0.0F);
        } else {
            float xRot = state.xRot;
            float rotation = state.yRot;
            if (state.posOnRail != null && state.frontPos != null && state.backPos != null) {
                poseStack.translate(
                        state.posOnRail.x - state.x,
                        (state.frontPos.y + state.backPos.y) / 2.0 - state.y,
                        state.posOnRail.z - state.z);
                Vec3 dir = state.backPos.add(-state.frontPos.x, -state.frontPos.y, -state.frontPos.z);
                if (dir.length() != 0.0) {
                    dir = dir.normalize();
                    rotation = (float)(Math.atan2(dir.z, dir.x) * 180.0 / Math.PI);
                    xRot = (float)(Math.atan(dir.y) * 73.0);
                }
            }
            poseStack.translate(0.0F, 0.375F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotation));
            poseStack.mulPose(Axis.ZP.rotationDegrees(-xRot));
        }

        // Hurt wobble
        float hurt = state.hurtTime;
        if (hurt > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(
                    Mth.sin((double) hurt) * hurt * state.damageTime / 10.0F * state.hurtDir));
        }

        // Flip axes to match vanilla minecart orientation convention
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // Submit our model with our texture instead of the hardcoded vanilla one
        submitNodeCollector.submitModel(
                this.model,
                state,
                poseStack,
                TEXTURE,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                (ModelFeatureRenderer.CrumblingOverlay) null);

        poseStack.popPose();
    }

    @Override
    public Vec3 getRenderOffset(ShoppingCartRenderState state) {
        Vec3 offset = super.getRenderOffset(state);
        if (state.isNewRender && state.renderPos != null) {
            return offset.add(
                    state.renderPos.x - state.x,
                    state.renderPos.y - state.y,
                    state.renderPos.z - state.z);
        }
        return offset;
    }
}