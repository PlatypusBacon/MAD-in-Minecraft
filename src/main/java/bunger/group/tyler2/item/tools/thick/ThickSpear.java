package bunger.group.tyler2.item.tools.thick;

import bunger.group.tyler2.item.tools.ReachToolHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.AttackRange;

public class ThickSpear extends Item {

    public ThickSpear(ToolMaterial material, Item.Properties properties) {
        super(properties
                .spear(
                        material,
                        attackDuration(material),
                        damageMultiplier(material),
                        delay(material),
                        dismountTime(material),
                        dismountThreshold(material),
                        knockbackTime(material),
                        knockbackThreshold(material),
                        damageTime(material),
                        damageThreshold(material)
                )
                .component(DataComponents.ATTACK_RANGE, new AttackRange(1.0F, 2.0F, 1.0F, 2.0F, 0.125F, 0.5F))
                .attributes(ReachToolHelper.buildLongSpearModifiers(material, attackDuration(material)))
        );
    }

    private static float attackDuration(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 0.65f;
        if (m == ToolMaterial.STONE)     return 0.75f;
        if (m == ToolMaterial.IRON)      return 0.95f;
        if (m == ToolMaterial.GOLD)      return 0.95f;
        if (m == ToolMaterial.DIAMOND)   return 1.05f;
        if (m == ToolMaterial.NETHERITE) return 1.15f;
        return 0.85f; // COPPER / fallback
    }

    private static float damageMultiplier(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 0.7f;
        if (m == ToolMaterial.STONE)     return 0.82f;
        if (m == ToolMaterial.IRON)      return 0.95f;
        if (m == ToolMaterial.GOLD)      return 0.7f;
        if (m == ToolMaterial.DIAMOND)   return 1.075f;
        if (m == ToolMaterial.NETHERITE) return 1.2f;
        return 0.82f; // COPPER / fallback
    }

    private static float delay(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 0.75f;
        if (m == ToolMaterial.STONE)     return 0.7f;
        if (m == ToolMaterial.IRON)      return 0.6f;
        if (m == ToolMaterial.GOLD)      return 0.7f;
        if (m == ToolMaterial.DIAMOND)   return 0.5f;
        if (m == ToolMaterial.NETHERITE) return 0.4f;
        return 0.65f; // COPPER / fallback
    }

    private static float dismountTime(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 5.0f;
        if (m == ToolMaterial.STONE)     return 4.5f;
        if (m == ToolMaterial.IRON)      return 2.5f;
        if (m == ToolMaterial.GOLD)      return 3.5f;
        if (m == ToolMaterial.DIAMOND)   return 3.0f;
        if (m == ToolMaterial.NETHERITE) return 2.5f;
        return 4.0f; // COPPER / fallback
    }

    private static float dismountThreshold(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 14.0f;
        if (m == ToolMaterial.STONE)     return 13.0f;
        if (m == ToolMaterial.IRON)      return 11.0f;
        if (m == ToolMaterial.GOLD)      return 13.0f;
        if (m == ToolMaterial.DIAMOND)   return 10.0f;
        if (m == ToolMaterial.NETHERITE) return 9.0f;
        return 12.0f; // COPPER / fallback
    }

    private static float knockbackTime(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 10.0f;
        if (m == ToolMaterial.STONE)     return 9.0f;
        if (m == ToolMaterial.IRON)      return 6.75f;
        if (m == ToolMaterial.GOLD)      return 8.5f;
        if (m == ToolMaterial.DIAMOND)   return 6.5f;
        if (m == ToolMaterial.NETHERITE) return 5.5f;
        return 8.25f; // COPPER / fallback
    }

    private static float knockbackThreshold(ToolMaterial m) {
        return 5.1f; // same for all tiers
    }

    private static float damageTime(ToolMaterial m) {
        if (m == ToolMaterial.WOOD)      return 15.0f;
        if (m == ToolMaterial.STONE)     return 13.75f;
        if (m == ToolMaterial.IRON)      return 11.25f;
        if (m == ToolMaterial.GOLD)      return 13.75f;
        if (m == ToolMaterial.DIAMOND)   return 10.0f;
        if (m == ToolMaterial.NETHERITE) return 8.75f;
        return 12.5f; // COPPER / fallback
    }

    private static float damageThreshold(ToolMaterial m) {
        return 4.6f; // same for all tiers
    }
}