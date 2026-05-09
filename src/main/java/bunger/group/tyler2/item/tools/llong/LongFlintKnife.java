package bunger.group.tyler2.item.tools.llong;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class LongFlintKnife extends Item {
    public LongFlintKnife(ToolMaterial material, Properties properties) {
        super(properties
                .sword(material, 1.5f, -2.2f)
                .attributes(ReachToolHelper.buildLongModifiers(material, 1.5f, -2.2f, true))

        );
    }
}
