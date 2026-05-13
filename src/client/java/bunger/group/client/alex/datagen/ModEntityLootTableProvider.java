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

                // Roll 2: Drop 1-2 gunpower always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GUNPOWDER)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 2.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )
        );

        add(ModEntityTypes.GOBLIN_GRUNT, LootTable.lootTable()

                // ── Roll 1: Pure Mana (10% chance, +5% per looting level)  2 Rolls ─────────────
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2.0f))
                        .add(LootItem.lootTableItem(ModItems.PURE_MANA)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.10f, 0.05f))
                )

                // Roll 2: Drop 0-4 Gold Nuggets always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0f, 4.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )
        );

        add(ModEntityTypes.GOBLIN_MAGE, LootTable.lootTable()

                // ── Roll 1: Pure Mana (50% chance, +10% per looting level)  4 Rolls ─────────────
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(4.0f))
                        .add(LootItem.lootTableItem(ModItems.PURE_MANA)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.50f, 0.10f))
                )

                // Roll 2: Drop 0-4 Gold Nuggets always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0f, 4.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )

                // Roll 3; Maybe poison scroll
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.075f, 0.0f))
                )
        );

        add(ModEntityTypes.GOBLIN_RANGER, LootTable.lootTable()

                // ── Roll 1: Pure Mana (10% chance, +5% per looting level)  2 Rolls ─────────────
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2.0f))
                        .add(LootItem.lootTableItem(ModItems.PURE_MANA)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.10f, 0.05f))
                )

                // Roll 2: Drop a 0-2 Arrows always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.ARROW)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0f, 2.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )

                // Roll 3: Drop 0-4 Gold Nuggets always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0f, 4.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )
        );

        add(ModEntityTypes.GOBLIN_CHIEF, LootTable.lootTable()

                // ── Roll 1: Pure Mana (20% chance, +5% per looting level)  3 Rolls ─────────────
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(3.0f))
                        .add(LootItem.lootTableItem(ModItems.PURE_MANA)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0f, 1.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.20f, 0.05f))
                )

                // Roll 2: Drop a 10-30 Gold Nuggets always
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(10.0f, 30.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 1f, 0.20f))
                )

                // Roll 3: Goblin crown ~5% of the time
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.GOBLIN_CROWN)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0f, 2.0f))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.05f, 0.0f))
                )
        );
    }
}