package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ThickFlintKnife extends Item {
    public ThickFlintKnife(ToolMaterial material, Properties properties) {
        super(properties
                .sword(material, 1.5f, -2.2f)
                .attributes(ReachToolHelper.buildThickModifiers(material, 1.5f, -2.2f, true))

        );
    }
}
