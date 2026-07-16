package bunger.group.bryan;
import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.network.Filterable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;


public class TaxLogic {

    public static final Map<UUID, TaxData> PLAYER_TAXES = new HashMap<>();

    public static void applyTaxBook(ItemStack itemStack, Player user) {
        List<Item> allItems = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(allItems::add);

        Collections.shuffle(allItems);

        Random random = new Random();

        Map<Item, Integer> taxMap = new LinkedHashMap<>();

        /*
        1x type of wood or wood item (1-64)
        1x pickaxe obtainable stone (1-64)
        1x food item or farming item (1-64)
        1x ore (1-32) (current pickaxe?)
        1x random (1-16) (now nuggets)
        */
        
        TagKey<Item> WOOD = MutuallyAssuredDestruction.WOOD_TAXES;
        TagKey<Item> STONE = MutuallyAssuredDestruction.STONE_TAXES;
        TagKey<Item> FOOD = MutuallyAssuredDestruction.FOOD_TAXES;
        TagKey<Item> ORE = MutuallyAssuredDestruction.ORE_TAXES;
        TagKey<Item> RANDOM = ItemTags.METAL_NUGGETS;


        // select ore item first so we can adjust quantity for rare items
        Item oreItem = getRandomFromTag(ORE, random);
        int numOre = random.nextInt(32) + 1;
        // if the chosen ore is netherite scrap or ingot, limit to 1
        String oreId = BuiltInRegistries.ITEM.getKey(oreItem).toString();
        if (oreId.equals("minecraft:netherite_scrap") || oreId.equals("minecraft:netherite_ingot")) {
            numOre = 1;
        }

        taxMap.put(getRandomFromTag(WOOD, random), random.nextInt(64) + 1);
        taxMap.put(getRandomFromTag(STONE, random), random.nextInt(64) + 1);
        taxMap.put(getRandomFromTag(FOOD, random), random.nextInt(64) + 1);
        taxMap.put(oreItem, numOre);
        taxMap.put(getRandomFromTag(RANDOM, random), random.nextInt(64) + 1);

        StringBuilder text = new StringBuilder();


        Map<String, Integer> requiredItems = new HashMap<>();

        for (Map.Entry<Item, Integer> entry : taxMap.entrySet()) {

            String itemId = BuiltInRegistries.ITEM.getKey(entry.getKey()).toString();

            requiredItems.put(itemId, entry.getValue());
            // System.out.println("!!!!!!!!!!"+entry.getValue());
        }

        for (Map.Entry<Item, Integer> entry : taxMap.entrySet()) {
            String name = entry.getKey().getName(entry.getKey().getDefaultInstance()).getString();
            int count = entry.getValue();

            text.append("- ").append(count).append("x ").append(name).append("\n");
        }

        WrittenBookContent content = new WrittenBookContent(
            Filterable.passThrough("CP14 Notice of Assessment"),
            "The IRS",
            0,
            List.of(
                Filterable.passThrough(Component.literal("To : " + user.getName().getString() + "\nFrom: The IRS\n\n Tax payments are due...\n\nPay owed items in full in the next 10 business days and no further penalties will be incurred.")),
            
                Filterable.passThrough(Component.literal("Items owed to the IRS...\n\n" + text.toString()))
            ),
            false
        );


        long currentDay = user.level().getGameTime() / 24000L;
        long dueDay = currentDay+5;
        System.out.println("Current Day: " + currentDay + " Due Day: " + dueDay);

        PLAYER_TAXES.put(
            user.getUUID(),
            new TaxData(requiredItems, dueDay)
        );

        itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, content);

    }


    public static Item getRandomFromTag(TagKey<Item> tag, Random random) {

        List<Item> items = BuiltInRegistries.ITEM.stream()
            .filter(item -> item.builtInRegistryHolder().is(tag))
            .toList();

        return items.get(random.nextInt(items.size()));
    }


    public static void checkTaxes(ServerPlayer player, MinecraftServer server) {

        player.sendSystemMessage(Component.literal("Checking taxes..."));

        TaxData data = PLAYER_TAXES.get(player.getUUID());

        if (data == null){
            return;
        }

        long currentDay = player.level().getGameTime() / 24000L;

        if (currentDay >= data.dueDay) { //less than or equal otherwise you get taxes before previous one due?
            if(data.paid){
                player.sendSystemMessage(Component.literal(player.getName().getString() +" paid their taxes"));
            } else {
                player.sendSystemMessage(Component.literal(player.getName().getString() +" failed to pay their taxes... "));
                
                punishPlayer(player, server);
                data.paid = true;
            }
        }
    }

    public static void punishPlayer(Player player, MinecraftServer server) {

        // ============ HANDLES ALL BLOCKS INCDLUING BARRELS ETC ============
        for (var entry : ChestTracker.getChests().entrySet()) {

            ResourceKey<Level> dim = entry.getKey();
            Set<BlockPos> positions = entry.getValue();

            ServerLevel level = server.getLevel(dim);
            if (level == null) continue;

            for (BlockPos pos : positions) {

                BlockEntity be = level.getBlockEntity(pos);

                if (be instanceof Container container) {
                    TaxData data = TaxLogic.PLAYER_TAXES.get(player.getUUID());
                    int numItemsToRemove = data.requiredItems.size();
                    removeItemsFromContainer(container, numItemsToRemove, player);
                }
            }
        }

        // ============ HANDLES ALL ENTITIES SUCH AS MINECARTS ETC ============
        for (UUID uuid : StorageEntityTracker.STORAGE_ENTITIES) {

            for (ServerLevel level : server.getAllLevels()) {

                Entity entity = level.getEntity(uuid);

                if (entity instanceof Container container) {
                    TaxData data = TaxLogic.PLAYER_TAXES.get(player.getUUID());
                    int numItemsToRemove = data.requiredItems.size();
                    removeItemsFromContainer(container, numItemsToRemove, player);
                }
            }
        }
    }

    public static void removeItemsFromContainer(Container container, int amount, Player player) {

        Random random = new Random();

        for (int i = 0; i < amount; i++) {

            int slot = random.nextInt(container.getContainerSize());
            ItemStack stack = container.getItem(slot);

            if (!stack.isEmpty()) {

                String itemName = stack.getHoverName().getString();
                int itemCount = stack.getCount();

                ItemStack notice = new ItemStack(NoticeItem.NOTICE_ITEM);
                WrittenBookContent content = new WrittenBookContent(
                    Filterable.passThrough("Notice of Asset Seizure"),
                    "The IRS",
                    0,
                    List.of(
                        Filterable.passThrough(Component.literal(
                            "NOTICE OF ASSET SEIZURE\n\n" +
                            "Due to unpaid taxes, the IRS has seized the following property from "
                            + player.getName().getString() + ".\n\n" +
                            "Seized Item:\n" +
                            itemCount + "x " + itemName
                        ))
                    ),
                    false
                );
                notice.set(DataComponents.WRITTEN_BOOK_CONTENT, content);

                container.setItem(slot, notice);

                container.setChanged();
            }
        }
    }
}
