package bunger.group.tyler2;

// You'll need to add these BlockTags/ItemTags constants somewhere accessible
// since they're mod-defined, not vanilla. Best place is a ModTags class:

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> INCORRECT_FOR_ANTLER_TOOL =
            TagKey.create(Registries.BLOCK,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "incorrect_for_antler_tool"));

    public static final TagKey<Item> ANTLER_TOOL_MATERIALS =
            TagKey.create(Registries.ITEM,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "antler_tool_materials"));
}