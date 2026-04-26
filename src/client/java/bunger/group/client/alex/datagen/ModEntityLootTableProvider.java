package bunger.group.client.alex.datagen;

import bunger.group.alex.entity.ModEntityTypes;
import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricEntityLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class ModEntityLootTableProvider extends FabricEntityLootSubProvider {

    public ModEntityLootTableProvider(FabricPackOutput output,
                                      CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate() {
        add(ModEntityTypes.WRAITH, LootTable.lootTable()

                // ── Roll 1: Pure Mana (33% chance, +7% per looting level)  2 Rolls ─────────────
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2.0f))
                        .add(LootItem.lootTableItem(ModItems.PURE_MANA)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.33f, 0.07f))
                )

                // Roll 3: Drop a 1-2 gunpower always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GUNPOWDER)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 2.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )
        );
    }
}