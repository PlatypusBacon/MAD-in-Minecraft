package bunger.group.alex.item.spell;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.WeatherData;

public class InvokeRain extends SpellTemplate {
    public InvokeRain(Properties properties) {
        super(properties, 110, 1, SpellTypes.WATER);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            WeatherData weather = serverLevel.getWeatherData();
            weather.setRaining(true);
            weather.setRainTime(6000);
            weather.setThundering(false);
            weather.setThunderTime(0);
            weather.setClearWeatherTime(0);
        }
    }
}
