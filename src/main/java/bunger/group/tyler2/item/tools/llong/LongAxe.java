package bunger.group.tyler2.item.tools.llong;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class LongAxe extends Item {
    public LongAxe(ToolMaterial material, Item.Properties properties) {
        super(properties
                .axe(material, 5.0f, -3.0f)
                .attributes(ReachToolHelper.buildLongModifiers(material, 5.0f, -3.0f, true))
        );
    }
}