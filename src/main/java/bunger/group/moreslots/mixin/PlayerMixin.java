package bunger.group.moreslots.mixin;

import bunger.group.moreslots.api.SlotTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void dropExtraSlot(ServerLevel level, CallbackInfo ci) {
        Player player = (Player)(Object)this;
        if (!(Boolean)level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
            ItemStack stack = player.getAttachedOrElse(SlotTypes.EQUIPMENT_SLOT, ItemStack.EMPTY);
            if (!stack.isEmpty()) {
                player.drop(stack, true);
                player.setAttached(SlotTypes.EQUIPMENT_SLOT, ItemStack.EMPTY);
            }
        }
    }
}