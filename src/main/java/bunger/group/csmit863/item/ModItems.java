package bunger.group.csmit863.item;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.csmit863.effects.HallucinationEffect;
import bunger.group.csmit863.effects.MadnessEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;

import java.util.function.Function;

public class ModItems {

    public static final Holder<MobEffect> HALLUCINATION_EFFECT =
            Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "hallucination"),
                    new HallucinationEffect().setBlendDuration(150, 20, 60));

    public static final Holder<MobEffect> MADNESS_EFFECT =
            Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "madness"),
                    new MadnessEffect());

    public static final Consumable MAGIC_MUSHROOM_CONSUMABLE = Consumables.defaultFood()
            .build();

    public static final FoodProperties MAGIC_MUSHROOM_FOOD = new FoodProperties.Builder()
            .alwaysEdible()
            .build();

    public static final Item MAGIC_MUSHROOM = registerItem(
            "magic_mushroom",
            MagicMushroom::new,
            new Item.Properties().food(MAGIC_MUSHROOM_FOOD, MAGIC_MUSHROOM_CONSUMABLE)
    );

    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void register() {}
}