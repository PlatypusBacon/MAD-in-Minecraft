package bunger.group.bryan;
import bunger.group.MutuallyAssuredDestruction;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import java.util.function.Function;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;



public class NoticeItem extends WrittenBookItem {

    public NoticeItem(Item.Properties properties) {
        super(properties);
    }
    
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
          ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
    
          T item = itemFactory.apply(settings.setId(itemKey));
    
          Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    
          return item;
    }
    
    public static final Item NOTICE_ITEM = register("notice_item", NoticeItem::new, new Item.Properties());

    public static void initialize() {
            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(NoticeItem.NOTICE_ITEM));
    }

    
    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide()) {

            if (!stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
                NoticeItem.applyNoticeBook(stack, user);
            }

            user.openItemGui(stack, hand);
        }

        return InteractionResult.SUCCESS;
    }

    public static void applyNoticeBook(ItemStack stack, Player user){

    }
}