package bunger.group.tyler2.item.tools.llong;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class LongShovel extends Item {
    public LongShovel(ToolMaterial material, Item.Properties properties) {
        super(properties
                .shovel(material, 1.5f, -3.0f)
                .attributes(ReachToolHelper.buildLongModifiers(material, 1.5f, -3.0f, true))
        );
    }
}