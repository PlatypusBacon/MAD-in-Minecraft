package bunger.group.tyler2.item.tools;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import bunger.group.MutuallyAssuredDestruction;

public class ModToolProperties {

    public static final Identifier THICK_REACH_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "thick_tool_reach");
    public static final Identifier LONG_REACH_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "long_tool_reach");

    // Call this on the properties AFTER applyToolProperties has been called,
    // by wrapping the item construction — see ModItems below
    public static Item.Properties withThickReach(Item.Properties props) {
        // We need to grab the existing modifiers and append to them
        // This is done by providing a custom component via a wrapper item
        // See note — handled in the Item subclass instead
        return props;
    }
}