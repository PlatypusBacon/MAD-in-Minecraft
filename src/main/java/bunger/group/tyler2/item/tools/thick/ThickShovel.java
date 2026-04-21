package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ThickShovel extends Item {
    public ThickShovel(ToolMaterial material, Item.Properties properties) {
        super(properties
                .shovel(material, 1.5f, -3.0f)
                .attributes(ReachToolHelper.buildThickModifiers(material, 1.5f, -3.0f, true))
        );
    }
}