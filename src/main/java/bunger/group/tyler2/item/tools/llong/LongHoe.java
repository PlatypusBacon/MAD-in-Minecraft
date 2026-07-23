package bunger.group.tyler2.item.tools.llong;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class LongHoe extends Item {
    public LongHoe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .hoe(material, 0.0f, -3.0f)
                .attributes(ReachToolHelper.buildLongModifiers(material, 0.0f, -3.0f, true))
        );
    }
}