package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ThickHoe extends Item {
    public ThickHoe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .hoe(material, 0.0f, -3.0f)
                .attributes(ReachToolHelper.buildThickModifiers(material, 0.0f, -3.0f, true))
        );
    }
}