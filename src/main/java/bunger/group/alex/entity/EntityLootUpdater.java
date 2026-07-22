package bunger.group.alex.entity;

import bunger.group.alex.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class EntityLootUpdater {

    public static void update_loot_pools() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;

            if (key.equals(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/zombie")))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.ZOMBIE_LEGGINGS))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.002f, 0.001f))
                );
            }


            if (key.equals(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/skeleton")))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.SKELETON_CHESTPLATE))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.002f, 0.001f))
                );
            }

            if (key.equals(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/spider")))) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(ModItems.SPIDER_BOOTS))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition
                                .randomChanceAndLootingBoost(registries, 0.002f, 0.001f))
                );
            }
        });
    }
}
