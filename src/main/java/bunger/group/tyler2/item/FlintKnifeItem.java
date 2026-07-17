package bunger.group.tyler2.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class FlintKnifeItem extends Item {
    public FlintKnifeItem(ToolMaterial material, Properties properties) {
        super(properties
                .sword(material, 1.5f, -2.2f)
        );
    }
}
