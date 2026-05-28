package bunger.group.client.alex.datagen;

import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.CLOTH_HELMET)
                .add(ModItems.CLOTH_CHESTPLATE)
                .add(ModItems.CLOTH_LEGGINGS)
                .add(ModItems.CLOTH_BOOTS)

                .add(ModItems.GOBLIN_CROWN)

                .add(ModItems.ZOMBIE_LEGGINGS)
                .add(ModItems.SKELETON_CHESTPLATE)
                .add(ModItems.SPIDER_BOOTS);

        valueLookupBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ModItems.CLOTH_BOOTS)
                .add(ModItems.SPIDER_BOOTS);

        valueLookupBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(ModItems.CLOTH_LEGGINGS)
                .add(ModItems.ZOMBIE_LEGGINGS);

        valueLookupBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(ModItems.CLOTH_CHESTPLATE)
                .add(ModItems.SKELETON_CHESTPLATE);

        valueLookupBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(ModItems.CLOTH_HELMET)
                .add(ModItems.GOBLIN_CROWN);

        valueLookupBuilder(ItemTags.BOW_ENCHANTABLE)
                .add((ModItems.LONGBOW));
    }
}