package bunger.group.tyler2.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class IronHogItem extends Item{
        public IronHogItem(ToolMaterial material, Properties properties) {
            super(properties
                    .sword(material, 5.0f, -2.5f)
            );
        }
    }

