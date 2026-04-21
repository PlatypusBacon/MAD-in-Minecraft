package bunger.group.tyler2.item.tools.llong;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class LongSword extends Item {
    public LongSword(ToolMaterial material, Item.Properties properties) {
        super(material.applySwordProperties(
                properties,
                3.0f,
                -2.4f
        ));
    }
}