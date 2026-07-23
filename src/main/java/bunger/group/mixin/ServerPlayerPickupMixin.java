package bunger.group.mixin;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import bunger.group.tyler3.rego.RecipePageRegistry;

@Mixin(ServerPlayer.class)
public class ServerPlayerPickupMixin {

    @Inject(method = "take", at = @At("HEAD"))
    private void onTake(Entity entity, int count, CallbackInfo ci) {
        if (!(entity instanceof ItemEntity itemEntity)) return;

        ServerPlayer self = (ServerPlayer)(Object) this;
        Item item = itemEntity.getItem().getItem();
        MutuallyAssuredDestruction.LOGGER.info("[Tome] take() fired for item: {}", item);

        RecipePageRegistry.getRecipeIdForItem(item).ifPresent(recipeId -> {
            MutuallyAssuredDestruction.LOGGER.info("[Tome] Sending unlock packet for: {}", recipeId);
            ServerPlayNetworking.send(self, new UnlockRecipePagePayload(recipeId));
        });
    }
}