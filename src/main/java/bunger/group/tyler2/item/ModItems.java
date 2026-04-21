package bunger.group.tyler2.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler2.item.tools.*;
import bunger.group.tyler2.item.tools.llong.*;
import bunger.group.tyler2.item.tools.thick.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {

    // --- Sticks ---
    public static final Item THICK_STICK = register("thick_stick", Item::new, new Item.Properties());
    public static final Item LONG_STICK  = register("long_stick",  Item::new, new Item.Properties());

    // --- Thick tools ---
    public static final Item THICK_WOODEN_PICKAXE    = register("thick_wooden_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_WOOD, p),    new Item.Properties());
    public static final Item THICK_STONE_PICKAXE     = register("thick_stone_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_STONE, p),   new Item.Properties());
    public static final Item THICK_IRON_PICKAXE      = register("thick_iron_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_IRON, p),    new Item.Properties());
    public static final Item THICK_GOLD_PICKAXE    = register("thick_gold_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_GOLD, p),    new Item.Properties());
    public static final Item THICK_DIAMOND_PICKAXE   = register("thick_diamond_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_DIAMOND, p), new Item.Properties());
    public static final Item THICK_NETHERITE_PICKAXE = register("thick_netherite_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_NETHERITE, p),new Item.Properties());
    public static final Item THICK_COPPER_PICKAXE = register("thick_copper_pickaxe", p -> new ThickPickaxe(ModToolMaterials.THICK_COPPER, p),new Item.Properties());

    public static final Item THICK_WOODEN_SWORD     = register("thick_wooden_sword",     p -> new ThickSword(ModToolMaterials.THICK_WOOD,p),       new Item.Properties());
    public static final Item THICK_STONE_SWORD      = register("thick_stone_sword",      p -> new ThickSword(ModToolMaterials.THICK_STONE, p),      new Item.Properties());
    public static final Item THICK_IRON_SWORD       = register("thick_iron_sword",       p -> new ThickSword(ModToolMaterials.THICK_IRON,p),       new Item.Properties());
    public static final Item THICK_GOLD_SWORD     = register("thick_gold_sword",     p -> new ThickSword(ModToolMaterials.THICK_GOLD,p),       new Item.Properties());
    public static final Item THICK_DIAMOND_SWORD    = register("thick_diamond_sword",    p -> new ThickSword(ModToolMaterials.THICK_DIAMOND,p),    new Item.Properties());
    public static final Item THICK_NETHERITE_SWORD  = register("thick_netherite_sword",  p -> new ThickSword(ModToolMaterials.THICK_NETHERITE,p),  new Item.Properties());
    public static final Item THICK_COPPER_SWORD  = register("thick_copper_sword",  p -> new ThickSword(ModToolMaterials.THICK_COPPER,p),  new Item.Properties());

    public static final Item THICK_WOODEN_AXE    = register("thick_wooden_axe", p -> new ThickAxe(ModToolMaterials.THICK_WOOD, p),    new Item.Properties());
    public static final Item THICK_STONE_AXE     = register("thick_stone_axe", p -> new ThickAxe(ModToolMaterials.THICK_STONE, p),   new Item.Properties());
    public static final Item THICK_IRON_AXE      = register("thick_iron_axe", p -> new ThickAxe(ModToolMaterials.THICK_IRON, p),    new Item.Properties());
    public static final Item THICK_GOLD_AXE    = register("thick_gold_axe", p -> new ThickAxe(ModToolMaterials.THICK_GOLD, p),    new Item.Properties());
    public static final Item THICK_DIAMOND_AXE   = register("thick_diamond_axe", p -> new ThickAxe(ModToolMaterials.THICK_DIAMOND, p), new Item.Properties());
    public static final Item THICK_NETHERITE_AXE = register("thick_netherite_axe", p -> new ThickAxe(ModToolMaterials.THICK_NETHERITE, p),new Item.Properties());
    public static final Item THICK_COPPER_AXE  = register("thick_copper_axe",  p -> new ThickAxe(ModToolMaterials.THICK_COPPER,p),  new Item.Properties());

    public static final Item THICK_WOODEN_SPEAR    = register("thick_wooden_spear",    p -> new ThickSpear(ModToolMaterials.THICK_WOOD,p),       new Item.Properties());
    public static final Item THICK_STONE_SPEAR   = register("thick_stone_spear",     p -> new ThickSpear(ModToolMaterials.THICK_STONE,p),      new Item.Properties());
    public static final Item THICK_IRON_SPEAR      = register("thick_iron_spear",      p -> new ThickSpear(ModToolMaterials.THICK_IRON,p),       new Item.Properties());
    public static final Item THICK_GOLD_SPEAR    = register("thick_gold_spear",    p -> new ThickSpear(ModToolMaterials.THICK_GOLD,p),       new Item.Properties());
    public static final Item THICK_DIAMOND_SPEAR   = register("thick_diamond_spear",   p -> new ThickSpear(ModToolMaterials.THICK_DIAMOND,p),    new Item.Properties());
    public static final Item THICK_NETHERITE_SPEAR = register("thick_netherite_spear", p -> new ThickSpear(ModToolMaterials.THICK_NETHERITE,p),  new Item.Properties());
    public static final Item THICK_COPPER_SPEAR = register("thick_copper_spear", p -> new ThickSpear(ModToolMaterials.THICK_COPPER,p),  new Item.Properties());

    public static final Item THICK_WOODEN_SHOVEL    = register("thick_wooden_shovel",    p -> new ThickShovel(ModToolMaterials.THICK_WOOD,p),       new Item.Properties());
    public static final Item THICK_STONE_SHOVEL   = register("thick_stone_shovel",     p -> new ThickShovel(ModToolMaterials.THICK_STONE,p),      new Item.Properties());
    public static final Item THICK_IRON_SHOVEL     = register("thick_iron_shovel",      p -> new ThickShovel(ModToolMaterials.THICK_IRON,p),       new Item.Properties());
    public static final Item THICK_GOLD_SHOVEL   = register("thick_gold_shovel",    p -> new ThickShovel(ModToolMaterials.THICK_GOLD,p),       new Item.Properties());
    public static final Item THICK_DIAMOND_SHOVEL  = register("thick_diamond_shovel",   p -> new ThickShovel(ModToolMaterials.THICK_DIAMOND,p),    new Item.Properties());
    public static final Item THICK_NETHERITE_SHOVEL = register("thick_netherite_shovel", p -> new ThickShovel(ModToolMaterials.THICK_NETHERITE,p),  new Item.Properties());
    public static final Item THICK_COPPER_SHOVEL= register("thick_copper_shovel", p -> new ThickShovel(ModToolMaterials.THICK_COPPER,p),  new Item.Properties());

    public static final Item THICK_WOODEN_HOE   = register("thick_wooden_hoe",    p -> new ThickHoe(ModToolMaterials.THICK_WOOD,p),       new Item.Properties());
    public static final Item THICK_STONE_HOE   = register("thick_stone_hoe",     p -> new ThickHoe(ModToolMaterials.THICK_STONE,p),      new Item.Properties());
    public static final Item THICK_IRON_HOE     = register("thick_iron_hoe",      p -> new ThickHoe(ModToolMaterials.THICK_IRON,p),       new Item.Properties());
    public static final Item THICK_GOLD_HOE   = register("thick_gold_hoe",    p -> new ThickHoe(ModToolMaterials.THICK_GOLD,p),       new Item.Properties());
    public static final Item THICK_DIAMOND_HOE  = register("thick_diamond_hoe",   p -> new ThickHoe(ModToolMaterials.THICK_DIAMOND,p),    new Item.Properties());
    public static final Item THICK_NETHERITE_HOE = register("thick_netherite_hoe", p -> new ThickHoe(ModToolMaterials.THICK_NETHERITE,p),  new Item.Properties());
    public static final Item THICK_COPPER_HOE= register("thick_copper_hoe", p -> new ThickHoe(ModToolMaterials.THICK_COPPER,p),  new Item.Properties());

    // --- Long tools ---
    public static final Item LONG_WOODEN_PICKAXE    = register("long_wooden_pickaxe",    p -> new LongPickaxe(ModToolMaterials.LONG_WOOD,p),       new Item.Properties());
    public static final Item LONG_STONE_PICKAXE     = register("long_stone_pickaxe",     p -> new LongPickaxe(ModToolMaterials.LONG_STONE,p),      new Item.Properties());
    public static final Item LONG_IRON_PICKAXE      = register("long_iron_pickaxe",      p -> new LongPickaxe(ModToolMaterials.LONG_IRON,p),       new Item.Properties());
    public static final Item LONG_GOLD_PICKAXE    = register("long_gold_pickaxe",    p -> new LongPickaxe(ModToolMaterials.LONG_GOLD,p),       new Item.Properties());
    public static final Item LONG_DIAMOND_PICKAXE   = register("long_diamond_pickaxe",   p -> new LongPickaxe(ModToolMaterials.LONG_DIAMOND,p),    new Item.Properties());
    public static final Item LONG_NETHERITE_PICKAXE = register("long_netherite_pickaxe", p -> new LongPickaxe(ModToolMaterials.LONG_NETHERITE,p),  new Item.Properties());
    public static final Item LONG_COPPER_PICKAXE = register("long_copper_pickaxe", p -> new LongPickaxe(ModToolMaterials.LONG_COPPER,p),  new Item.Properties());

    public static final Item LONG_WOODEN_SHOVEL   = register("long_wooden_shovel",    p -> new LongShovel(ModToolMaterials.LONG_WOOD,p),       new Item.Properties());
    public static final Item LONG_STONE_SHOVEL     = register("long_stone_shovel",     p -> new LongShovel(ModToolMaterials.LONG_STONE,p),      new Item.Properties());
    public static final Item LONG_IRON_SHOVEL      = register("long_iron_shovel",      p -> new LongShovel(ModToolMaterials.LONG_IRON,p),       new Item.Properties());
    public static final Item LONG_GOLD_SHOVEL    = register("long_gold_shovel",    p -> new LongShovel(ModToolMaterials.LONG_GOLD,p),       new Item.Properties());
    public static final Item LONG_DIAMOND_SHOVEL   = register("long_diamond_shovel",   p -> new LongShovel(ModToolMaterials.LONG_DIAMOND,p),    new Item.Properties());
    public static final Item LONG_NETHERITE_SHOVEL = register("long_netherite_shovel", p -> new LongShovel(ModToolMaterials.LONG_NETHERITE,p),  new Item.Properties());
    public static final Item LONG_COPPER_SHOVEL = register("long_copper_shovel", p -> new LongShovel(ModToolMaterials.LONG_COPPER,p),  new Item.Properties());

    public static final Item LONG_WOODEN_HOE   = register("long_wooden_hoe",    p -> new LongHoe(ModToolMaterials.LONG_WOOD,p),       new Item.Properties());
    public static final Item LONG_STONE_HOE     = register("long_stone_hoe",     p -> new LongHoe(ModToolMaterials.LONG_STONE,p),      new Item.Properties());
    public static final Item LONG_IRON_HOE      = register("long_iron_hoe",      p -> new LongHoe(ModToolMaterials.LONG_IRON,p),       new Item.Properties());
    public static final Item LONG_GOLD_HOE    = register("long_gold_hoe",    p -> new LongHoe(ModToolMaterials.LONG_GOLD,p),       new Item.Properties());
    public static final Item LONG_DIAMOND_HOE   = register("long_diamond_hoe",   p -> new LongHoe(ModToolMaterials.LONG_DIAMOND,p),    new Item.Properties());
    public static final Item LONG_NETHERITE_HOE = register("long_netherite_hoe", p -> new LongHoe(ModToolMaterials.LONG_NETHERITE,p),  new Item.Properties());
    public static final Item LONG_COPPER_HOE = register("long_copper_hoe", p -> new LongHoe(ModToolMaterials.LONG_COPPER,p),  new Item.Properties());

    public static final Item LONG_WOODEN_SWORD      = register("long_wooden_sword",      p -> new LongSword(ModToolMaterials.LONG_WOOD,p),         new Item.Properties());
    public static final Item LONG_STONE_SWORD      = register("long_stone_sword",      p -> new LongSword(ModToolMaterials.LONG_STONE,p),         new Item.Properties());
    public static final Item LONG_IRON_SWORD      = register("long_iron_sword",      p -> new LongSword(ModToolMaterials.LONG_IRON,p),         new Item.Properties());
    public static final Item LONG_GOLD_SWORD      = register("long_gold_sword",      p -> new LongSword(ModToolMaterials.LONG_GOLD,p),         new Item.Properties());
    public static final Item LONG_NETHERITE_SWORD      = register("long_netherite_sword",      p -> new LongSword(ModToolMaterials.LONG_NETHERITE,p),         new Item.Properties());
    public static final Item LONG_COPPER_SWORD      = register("long_copper_sword",      p -> new LongSword(ModToolMaterials.LONG_COPPER,p),         new Item.Properties());
    public static final Item LONG_DIAMOND_SWORD      = register("long_diamond_sword",      p -> new LongSword(ModToolMaterials.LONG_DIAMOND,p),         new Item.Properties());

    public static final Item LONG_WOODEN_SPEAR    = register("long_wooden_spear",    p -> new LongSpear(ModToolMaterials.LONG_WOOD,p),       new Item.Properties());
    public static final Item LONG_STONE_SPEAR   = register("long_stone_spear",     p -> new LongSpear(ModToolMaterials.LONG_STONE,p),      new Item.Properties());
    public static final Item LONG_IRON_SPEAR      = register("long_iron_spear",      p -> new LongSpear(ModToolMaterials.LONG_IRON,p),       new Item.Properties());
    public static final Item LONG_GOLD_SPEAR    = register("long_gold_spear",    p -> new LongSpear(ModToolMaterials.LONG_GOLD,p),       new Item.Properties());
    public static final Item LONG_DIAMOND_SPEAR   = register("long_diamond_spear",   p -> new LongSpear(ModToolMaterials.LONG_DIAMOND,p),    new Item.Properties());
    public static final Item LONG_NETHERITE_SPEAR = register("long_netherite_spear", p -> new LongSpear(ModToolMaterials.LONG_NETHERITE,p),  new Item.Properties());
    public static final Item LONG_COPPER_SPEAR = register("long_copper_spear", p -> new LongSpear(ModToolMaterials.LONG_COPPER,p),  new Item.Properties());

    private static <T extends Item> T register(String name,
                                               java.util.function.Function<Item.Properties, T> factory,
                                               Item.Properties props) {
        ResourceKey<Item> key = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name)
        );
        T item = factory.apply(props.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void registerModItems() {
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering Mod Items for " + MutuallyAssuredDestruction.MOD_ID
        );
        // Touch sticks so their statics initialise
        var ts = THICK_STICK;
        var ls = LONG_STICK;
    }
}