package bunger.group.client.alex.datagen;

import java.util.concurrent.CompletableFuture;

import bunger.group.alex.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                // Cloth
                shaped(RecipeCategory.MISC, ModItems.CLOTH, 2)
                        .pattern(" w ")
                        .pattern("wew")
                        .pattern(" w ")
                        .define('w', ItemTags.WOOL) // Any wool
                        .define('e', ModItems.PURE_MANA) // Eitr
                        .unlockedBy(getHasName(ModItems.PURE_MANA), has(ModItems.PURE_MANA))
                        .save(output);

                // Cloth Armour
                shaped(RecipeCategory.COMBAT, ModItems.CLOTH_HELMET, 1)
                        .pattern("ccc")
                        .pattern("c c")
                        .define('c', ModItems.CLOTH) // Any wool
                        .unlockedBy(getHasName(ModItems.CLOTH), has(ModItems.CLOTH))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.CLOTH_CHESTPLATE, 1)
                        .pattern("c c")
                        .pattern("ccc")
                        .pattern("ccc")
                        .define('c', ModItems.CLOTH) // Any wool
                        .unlockedBy(getHasName(ModItems.CLOTH), has(ModItems.CLOTH))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.CLOTH_LEGGINGS, 1)
                        .pattern("ccc")
                        .pattern("c c")
                        .pattern("c c")
                        .define('c', ModItems.CLOTH) // Any wool
                        .unlockedBy(getHasName(ModItems.CLOTH), has(ModItems.CLOTH))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.CLOTH_BOOTS, 1)
                        .pattern("c c")
                        .pattern("c c")
                        .define('c', ModItems.CLOTH) // Any wool
                        .unlockedBy(getHasName(ModItems.CLOTH), has(ModItems.CLOTH))
                        .save(output);


                // Enchanted Chain
                shaped(RecipeCategory.MISC, ModItems.ENCHANTED_CHAIN, 1)
                        .pattern(" n ")
                        .pattern("ege")
                        .pattern(" n ")
                        .define('g', Items.GOLD_INGOT)
                        .define('n', Items.GOLD_NUGGET)
                        .define('e', ModItems.PURE_MANA) // Eitr
                        .unlockedBy(getHasName(ModItems.PURE_MANA), has(ModItems.PURE_MANA))
                        .save(output);

                // Enchanted Chain Armour
                shaped(RecipeCategory.COMBAT, ModItems.ENCHANTED_CHAIN_HELMET, 1)
                        .pattern("ccc")
                        .pattern("c c")
                        .define('c', ModItems.ENCHANTED_CHAIN)
                        .unlockedBy(getHasName(ModItems.ENCHANTED_CHAIN), has(ModItems.ENCHANTED_CHAIN))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.ENCHANTED_CHAIN_CHESTPLATE, 1)
                        .pattern("c c")
                        .pattern("ccc")
                        .pattern("ccc")
                        .define('c', ModItems.ENCHANTED_CHAIN)
                        .unlockedBy(getHasName(ModItems.ENCHANTED_CHAIN), has(ModItems.ENCHANTED_CHAIN))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.ENCHANTED_CHAIN_LEGGINGS, 1)
                        .pattern("ccc")
                        .pattern("c c")
                        .pattern("c c")
                        .define('c', ModItems.ENCHANTED_CHAIN) // Any wool
                        .unlockedBy(getHasName(ModItems.ENCHANTED_CHAIN), has(ModItems.ENCHANTED_CHAIN))
                        .save(output);
                shaped(RecipeCategory.COMBAT, ModItems.ENCHANTED_CHAIN_BOOTS, 1)
                        .pattern("c c")
                        .pattern("c c")
                        .define('c', ModItems.ENCHANTED_CHAIN) // Any wool
                        .unlockedBy(getHasName(ModItems.ENCHANTED_CHAIN), has(ModItems.ENCHANTED_CHAIN))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "AlexRecipeProvider";
    }
}