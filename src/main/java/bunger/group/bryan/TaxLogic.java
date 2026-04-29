package bunger.group.bryan;
import bunger.group.MutuallyAssuredDestruction;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class TaxLogic {

    public static void applyTaxBook(ItemStack itemStack, Player user) {
        List<Item> allItems = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(allItems::add);

        Collections.shuffle(allItems);

        List<Item> selected = allItems.subList(0, 5);

        StringBuilder text = new StringBuilder("");

        for (Item item : selected) {
            String name = item.getName(item.getDefaultInstance()).getString();
            text.append("- ").append(name).append("\n");
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

        String data = selected.stream()
            .map(item -> BuiltInRegistries.ITEM.getKey(item).toString())
            .reduce((a, b) -> a + "," + b)
            .orElse("");

        itemStack.set(MutuallyAssuredDestruction.TAX_ITEMS, data);

        // List<String> itemIds = selected.stream()
        //     .map(item -> BuiltInRegistries.ITEM.getKey(item).toString())
        //     .toList();

        // itemStack.set(MutuallyAssuredDestruction.TAX_ITEMS, itemIds);    

        itemStack.set(DataComponents.WRITTEN_BOOK_CONTENT, content);
    }
}
