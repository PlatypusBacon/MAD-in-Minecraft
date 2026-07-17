package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ThickSword extends Item {
    public ThickSword(ToolMaterial material, Item.Properties properties) {
        super(properties
                .sword(material, 3.0f, -2.4f)
                .attributes(ReachToolHelper.buildThickModifiers(material, 3.0f, -2.4f, false))
        );
    }
}