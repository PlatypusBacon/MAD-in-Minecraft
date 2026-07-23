package bunger.group.mixin;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import bunger.group.tyler3.rego.RecipePageRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityPickupMixin {

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void onPlayerTouch(Player player, CallbackInfo ci) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        ItemEntity self = (ItemEntity)(Object) this;

        // Only fire if the entity was actually consumed (fully picked up)
        if (self.isAlive()) return;

        Item item = self.getItem().getItem();

        RecipePageRegistry.getRecipeIdForItem(item).ifPresent(recipeId ->
                ServerPlayNetworking.send(serverPlayer, new UnlockRecipePagePayload(recipeId))
        );
    }
}