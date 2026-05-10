package bunger.group.tyler2;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties VENISON = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(0.3F)
            .build();
    public static final FoodProperties COOKED_VENISON = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.6F).build();
    public static final FoodProperties DEER_JERKY = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.5F).build();
    public static final FoodProperties BEEF_JERKY = new FoodProperties.Builder()
            .nutrition(7)
            .saturationModifier(0.7F).build();
    public static final FoodProperties PORK_JERKY = new FoodProperties.Builder()
            .nutrition(7)
            .saturationModifier(0.7F).build();
    public static final FoodProperties CHICKEN_JERKY = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.5F).build();
    public static final FoodProperties SHEEP_JERKY = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.6F).build();
    public static final FoodProperties DRIED_COD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.5F).build();
    public static final FoodProperties DRIED_SALMON = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.6F).build();
}