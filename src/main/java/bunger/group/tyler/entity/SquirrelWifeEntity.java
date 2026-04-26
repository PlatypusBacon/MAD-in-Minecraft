package bunger.group.tyler.entity;

import bunger.group.tyler.event.wife.Wife;
import bunger.group.tyler.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class SquirrelWifeEntity extends ArmorStand {

    private boolean triggered = false;

    public SquirrelWifeEntity(EntityType<? extends ArmorStand> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (stack.isEmpty()) return; // block removal

        boolean allowed = switch (slot) {
            case HEAD  -> stack.is(ModItems.SQUEATHER_HEAD);
            case CHEST -> stack.is(ModItems.SQUEATHER_CHEST);
            case LEGS  -> stack.is(ModItems.SQUEATHER_LEGS);
            case FEET  -> stack.is(ModItems.SQUEATHER_FEET);
            default    -> false;
        };

        if (allowed) {
            super.setItemSlot(slot, stack);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (triggered) return;
        if (level().isClientSide()) return;
        if (level().getGameTime() % 20 != 0) return;

        boolean headFilled  = this.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SQUEATHER_HEAD);
        boolean chestFilled = this.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.SQUEATHER_CHEST);
        boolean legsFilled  = this.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.SQUEATHER_LEGS);
        boolean feetFilled  = this.getItemBySlot(EquipmentSlot.FEET).is(ModItems.SQUEATHER_FEET);

        if (headFilled && chestFilled && legsFilled && feetFilled) {
            triggered = true;
            BlockPos pos = new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ());
            Wife.start((ServerLevel) level(), this);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (player.isSpectator()) return InteractionResult.SUCCESS;
        if (player.level().isClientSide()) return InteractionResult.SUCCESS_SERVER;

        ItemStack held = player.getItemInHand(hand);

        // Block taking armor off (empty hand click)
        if (held.isEmpty()) return InteractionResult.FAIL;

        // Only allow squeather items
        EquipmentSlot slot = this.getEquipmentSlotForItem(held);
        boolean allowed = switch (slot) {
            case HEAD  -> held.is(ModItems.SQUEATHER_HEAD);
            case CHEST -> held.is(ModItems.SQUEATHER_CHEST);
            case LEGS  -> held.is(ModItems.SQUEATHER_LEGS);
            case FEET  -> held.is(ModItems.SQUEATHER_FEET);
            default    -> false;
        };

        if (!allowed) return InteractionResult.FAIL;

        // Don't replace already-filled slots
        if (this.hasItemInSlot(slot)) return InteractionResult.FAIL;

        return super.interact(player, hand, location);
    }
}