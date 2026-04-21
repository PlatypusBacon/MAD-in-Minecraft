package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ThickAxe extends Item {
    public ThickAxe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .axe(material, 5.0f, -3.0f)
                .attributes(ReachToolHelper.buildThickModifiers(material, 5.0f, -3.0f, true))
        );
    }
}