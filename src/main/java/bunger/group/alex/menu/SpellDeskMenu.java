package bunger.group.alex.menu;

import bunger.group.alex.block.entity.SpellDeskEntity;
import bunger.group.alex.item.BlankScroll;
import bunger.group.alex.item.ModItems;
import bunger.group.alex.item.spell.SpellTypes;
import bunger.group.alex.spell.SpellDefinition;
import bunger.group.alex.spell.SpellRegistry;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpellDeskMenu extends AbstractContainerMenu {

    private final Container container;
    private final ContainerData data; // [0] = storedMana, [1] = scroll type ordinal (-1 = none)

    // Client-side
    public SpellDeskMenu(int containerId, Inventory inventory) {
        this(containerId, inventory,
                new SimpleContainer(SpellDeskEntity.CONTAINER_SIZE),
                new SimpleContainerData(2));
    }

    // Server-side
    public SpellDeskMenu(int containerId, Inventory inventory, SpellDeskEntity entity) {
        this(containerId, inventory, entity, new ContainerData() {
            @Override public int get(int i) {
                return switch (i) {
                    case 0 -> entity.getStoredMana();
                    case 1 -> {
                        ItemStack scroll = entity.getItem(SpellDeskEntity.BLANK_SCROLL_SLOT);
                        yield (!scroll.isEmpty() && scroll.getItem() instanceof BlankScroll bs)
                                ? bs.getType().ordinal() : -1;
                    }
                    default -> 0;
                };
            }
            @Override public void set(int i, int v) { if (i == 0) entity.setStoredMana(v); }
            @Override public int getCount() { return 2; }
        });
    }

    private SpellDeskMenu(int containerId, Inventory inventory,
                          Container container, ContainerData data) {
        super(ModMenuType.SPELL_DESK, containerId);
        this.container = container;
        this.data = data;

        checkContainerSize(container, SpellDeskEntity.CONTAINER_SIZE);
        container.startOpen(inventory.player);

        // Slot 0: BlankScroll  — MUST match SCROLL_SLOT_X / SCROLL_SLOT_Y in SpellDeskScreen
        this.addSlot(new Slot(container, SpellDeskEntity.BLANK_SCROLL_SLOT, 56, 36) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BlankScroll;
            }
        });

        // Slot 1: PureMana  — MUST match MANA_SLOT_X / MANA_SLOT_Y in SpellDeskScreen
        this.addSlot(new Slot(container, SpellDeskEntity.PURE_MANA_SLOT, 56, 89) {
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.PURE_MANA) && data.get(0) < SpellDeskEntity.MAX_MANA;
            }
        });

        // Standard inventory (3 rows + hotbar).
        // GUI_WIDTH=248, INV_X=(248-162)/2=43, INV_Y=145
        // These must match INV_X and INV_Y constants in SpellDeskScreen.
        this.addStandardInventorySlots(inventory, 43, 145);
        this.addDataSlots(data);
    }

    public boolean tryLearnSpell(Player player, int spellIndex) {
        List<SpellDefinition> available = getAvailableSpells();
        if (spellIndex < 0 || spellIndex >= available.size()) return false;

        SpellDefinition spell  = available.get(spellIndex);
        ItemStack       scroll = container.getItem(SpellDeskEntity.BLANK_SCROLL_SLOT);

        if (scroll.isEmpty() || !(scroll.getItem() instanceof BlankScroll)) return false;
        if (getStoredMana() < spell.manaCost()) return false;

        scroll.shrink(1);
        data.set(0, getStoredMana() - spell.manaCost());

        ItemStack result = new ItemStack(spell.result());
        if (!player.getInventory().add(result)) player.drop(result, false);
        return true;
    }

    public List<SpellDefinition> getAvailableSpells() {
        int typeOrdinal = data.get(1);
        if (typeOrdinal < 0 || typeOrdinal >= SpellTypes.values().length) return List.of();
        return SpellRegistry.getSpellsForType(SpellTypes.values()[typeOrdinal]);
    }

    public int getStoredMana()        { return data.get(0); }
    public int getScrollTypeOrdinal() { return data.get(1); }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack   = slot.getItem();
        ItemStack clicked = stack.copy();

        if (slotIndex < container.getContainerSize()) {
            if (!this.moveItemStackTo(stack, container.getContainerSize(), this.slots.size(), true))
                return ItemStack.EMPTY;
        } else if (!this.moveItemStackTo(stack, 0, container.getContainerSize(), false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return clicked;
    }

    @Override
    public boolean stillValid(Player player) { return container.stillValid(player); }
}