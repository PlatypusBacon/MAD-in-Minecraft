package bunger.group.tyler2.item.tools.thick;


import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ThickPickaxe extends Item {
    public ThickPickaxe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .pickaxe(material, 1.0f, -2.8f)
                .attributes(ReachToolHelper.buildThickPickaxeModifiers(material, 1.0f, -2.8f, true))
        );
    }
}