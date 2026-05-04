package bunger.group.client.tyler3.shopping_cart;

import net.minecraft.client.renderer.entity.state.MinecartRenderState;

/**
 * MinecartRenderState already contains everything we need:
 * yRot, xRot, hurtTime, hurtDir, damageTime, isNewRender,
 * posOnRail, frontPos, backPos, renderPos, offsetSeed,
 * displayOffset, displayBlockModel, outlineColor, lightCoords.
 *
 * Extend it here if you ever need to add shopping-cart-specific state.
 */
public class ShoppingCartRenderState extends MinecartRenderState {
}