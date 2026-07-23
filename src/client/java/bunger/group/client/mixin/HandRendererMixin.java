package bunger.group.client.mixin;

import bunger.group.tyler.data.BearBoxersSwingData;
import bunger.group.tyler.item.BearBoxersItem;
import bunger.group.tyler.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public abstract class HandRendererMixin {

    @Shadow
    abstract void renderArmWithItem(
            AbstractClientPlayer player, float partialTick, float pitch,
            InteractionHand hand, float swingProgress, ItemStack stack,
            float equippedProgress, PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector, int light
    );

    @Redirect(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
                    ordinal = 0
            )
    )
    private void redirectMainHand(ItemInHandRenderer instance,
                                  AbstractClientPlayer player, float partialTick, float pitch,
                                  InteractionHand hand, float swingProgress, ItemStack stack,
                                  float equippedProgress, PoseStack poseStack,
                                  SubmitNodeCollector submitNodeCollector, int light) {

        if (!player.getMainHandItem().is(ModItems.BEAR_BOXERS)) {
            this.renderArmWithItem(player, partialTick, pitch, hand,
                    swingProgress, stack, equippedProgress, poseStack, submitNodeCollector, light);
            return;
        }

        boolean offhandActive = BearBoxersSwingData.PUNCH_SIDE_CURRENT.getOrDefault(
                player.getUUID(), false);
        float finalSwing = offhandActive ? 0f : player.getAttackAnim(partialTick);

        this.renderArmWithItem(player, partialTick, pitch, hand,
                finalSwing, stack, equippedProgress, poseStack, submitNodeCollector, light);
    }

    @Redirect(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
                    ordinal = 1
            )
    )
    private void redirectOffHand(ItemInHandRenderer instance,
                                 AbstractClientPlayer player, float partialTick, float pitch,
                                 InteractionHand hand, float swingProgress, ItemStack stack,
                                 float equippedProgress, PoseStack poseStack,
                                 SubmitNodeCollector submitNodeCollector, int light) {

        if (!player.getMainHandItem().is(ModItems.BEAR_BOXERS)) {
            this.renderArmWithItem(player, partialTick, pitch, hand,
                    swingProgress, stack, equippedProgress, poseStack, submitNodeCollector, light);
            return;
        }

        boolean offhandActive = BearBoxersSwingData.PUNCH_SIDE_CURRENT.getOrDefault(
                player.getUUID(), false);
        float finalSwing = offhandActive ? player.getAttackAnim(partialTick) : 0f;

        this.renderArmWithItem(player, partialTick, pitch, hand,
                finalSwing, stack, equippedProgress, poseStack, submitNodeCollector, light);
    }
}