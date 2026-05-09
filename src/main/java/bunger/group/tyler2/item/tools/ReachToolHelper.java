package bunger.group.tyler2.item.tools;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ReachToolHelper {
    public static final double THICK_REACH_AMOUNT = -3.0;
    public static final double THICK_ENTITY_REACH = -2.0;
    public static final double LONG_ENTITY_REACH = 5.0;

    public static final double THICK_PICKAXE_REACH_AMOUNT = -4.0;
    public static final double LONG_REACH_AMOUNT  =  5.0;
    public static final Identifier THICK_REACH_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "thick_tool_reach");
    public static final Identifier LONG_REACH_ID =
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "long_tool_reach");

    public static ItemAttributeModifiers buildThickModifiers(
            ToolMaterial material, float attackDamage, float attackSpeed, boolean includeBlock) {
        return build(material, attackDamage, attackSpeed, THICK_REACH_ID, THICK_REACH_AMOUNT, THICK_ENTITY_REACH, includeBlock);
    }

    public static ItemAttributeModifiers buildThickPickaxeModifiers(
            ToolMaterial material, float attackDamage, float attackSpeed, boolean includeBlock) {
        return build(material, attackDamage, attackSpeed, THICK_REACH_ID, THICK_PICKAXE_REACH_AMOUNT, THICK_ENTITY_REACH,includeBlock);
    }

    public static ItemAttributeModifiers buildLongModifiers(
            ToolMaterial material, float attackDamage, float attackSpeed, boolean includeBlock) {
        return build(material, attackDamage, attackSpeed, LONG_REACH_ID, LONG_REACH_AMOUNT, LONG_ENTITY_REACH,includeBlock);
    }


    public static ItemAttributeModifiers buildLongSpearModifiers(ToolMaterial material, float attackDuration) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                                (double)(0.0f + material.attackDamageBonus()), Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                                (1.0f / attackDuration) - 4.0f, Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(LONG_REACH_ID, 4.0, Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }
    private static ItemAttributeModifiers build(
            ToolMaterial material, float attackDamage, float attackSpeed,
            Identifier reachId, double reachAmount, double entityReachAmount, boolean includeBlock) {

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        builder.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_ID,
                        (double)(attackDamage + material.attackDamageBonus()),
                        Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
        builder.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        Item.BASE_ATTACK_SPEED_ID,
                        (double)attackSpeed,
                        Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        AttributeModifier reachMod = new AttributeModifier(reachId, reachAmount, Operation.ADD_VALUE);
        AttributeModifier entityReachMod = new AttributeModifier(reachId, entityReachAmount, Operation.ADD_VALUE);

        builder.add(Attributes.ENTITY_INTERACTION_RANGE, entityReachMod, EquipmentSlotGroup.MAINHAND);
        if (includeBlock) {
            builder.add(Attributes.BLOCK_INTERACTION_RANGE, reachMod, EquipmentSlotGroup.MAINHAND);
        }

        return builder.build();
    }
}