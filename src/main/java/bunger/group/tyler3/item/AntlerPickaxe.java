package bunger.group.tyler3.item;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.spi.ToolProvider;

public class AntlerPickaxe extends Item {
    public AntlerPickaxe(ToolMaterial material, Properties properties) {
        super(properties
                .pickaxe(material, 1.0f, -2.8f)
        );
    }
}
