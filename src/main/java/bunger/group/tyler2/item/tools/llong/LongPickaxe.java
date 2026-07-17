package bunger.group.tyler2.item.tools.llong;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class LongPickaxe extends Item {
    public LongPickaxe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .pickaxe(material, 1.0f, -2.8f)
                .attributes(ReachToolHelper.buildLongModifiers(material, 1.0f, -2.8f, true))
        );
    }
}
