package bunger.group.tyler.enchantment;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> STAPELED = key("stapeled");
    private static ResourceKey<Enchantment> key(String path) {
        Identifier id = Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }
}
