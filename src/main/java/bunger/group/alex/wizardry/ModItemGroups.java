package bunger.group.alex.wizardry;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.alex.wizardry.items.spells.FireSpells;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class ModItemGroups {

    public static CreativeModeTab WIZARDRY_TAB;

    public static void register() {
        WIZARDRY_TAB = FabricItemGroupBuilder.build(
            new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "wizardry"),
            () -> new ItemStack(FireSpells.FIRE_IGNITION)
        );
    }
}