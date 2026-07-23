package bunger.group.tyler2.item.tools;

import bunger.group.tyler2.ModTags;
import bunger.group.tyler2.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

public class ModToolMaterials {

    // Thick variants — ~2.5x vanilla durability, +1 reach
    public static final ToolMaterial THICK_WOOD = new ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            147,    // 59 * 2.5
            2.0f,
            0.0f,
            15,
            ItemTags.WOODEN_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_STONE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            327,    // 131 * 2.5
            4.0f,
            1.0f,
            5,
            ItemTags.STONE_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_IRON = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            625,    // 250 * 2.5
            6.0f,
            2.0f,
            14,
            ItemTags.IRON_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_GOLD = new ToolMaterial(
            BlockTags.INCORRECT_FOR_GOLD_TOOL,
            80,     // 32 * 2.5
            12.0f,
            0.0f,
            22,
            ItemTags.GOLD_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_DIAMOND = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3902,   // 1561 * 2.5
            8.0f,
            3.0f,
            10,
            ItemTags.DIAMOND_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_NETHERITE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            5077,   // 2031 * 2.5
            9.0f,
            4.0f,
            15,
            ItemTags.NETHERITE_TOOL_MATERIALS
    );
    public static final ToolMaterial THICK_COPPER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_COPPER_TOOL,
            500,   // 2031 * 2.5
            6.0f,
            1.0f,
            13,
            ItemTags.COPPER_TOOL_MATERIALS
    );

    // Long variants — ~25% vanilla durability, high reach
    public static final ToolMaterial LONG_WOOD = new ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            2,
            2.0f,
            0.0f,
            15,
            ItemTags.WOODEN_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_STONE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            4,
            4.0f,
            1.0f,
            5,
            ItemTags.STONE_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_IRON = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            32,
            6.0f,
            2.0f,
            14,
            ItemTags.IRON_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_GOLD = new ToolMaterial(
            BlockTags.INCORRECT_FOR_GOLD_TOOL,
            1,
            12.0f,
            0.0f,
            22,
            ItemTags.GOLD_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_DIAMOND = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            120,
            8.0f,
            3.0f,
            10,
            ItemTags.DIAMOND_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_NETHERITE = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            256,
            9.0f,
            4.0f,
            15,
            ItemTags.NETHERITE_TOOL_MATERIALS
    );
    public static final ToolMaterial LONG_COPPER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_COPPER_TOOL,
            20,   // 2031 * 2.5
            4.0f,
            1.0f,
            13,
            ItemTags.COPPER_TOOL_MATERIALS
    );
    public static final ToolMaterial ANTLER = new ToolMaterial(
            ModTags.INCORRECT_FOR_ANTLER_TOOL,
            60,     // similar to wood
            2.0f,   // speed
            0.0f,   // attack damage bonus
            15,     // enchantability
            ModTags.ANTLER_TOOL_MATERIALS
    );

    public static final ToolMaterial THICK_ANTLER = new ToolMaterial(
            ModTags.INCORRECT_FOR_ANTLER_TOOL,
            147,
            2.0f,
            0.0f,
            15,
            ModTags.ANTLER_TOOL_MATERIALS
    );

    public static final ToolMaterial LONG_ANTLER = new ToolMaterial(
            ModTags.INCORRECT_FOR_ANTLER_TOOL,
            2,
            2.0f,
            0.0f,
            15,
            ModTags.ANTLER_TOOL_MATERIALS
    );
}