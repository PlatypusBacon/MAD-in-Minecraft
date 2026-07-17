package bunger.group.client.tyler3;

import com.mojang.serialization.MapCodec;
import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

public record IsFiring() implements ConditionalItemModelProperty {
    public static final MapCodec<IsFiring> MAP_CODEC = MapCodec.unit(new IsFiring());

    public boolean get(ItemStack stack, @Nullable ClientLevel level,
                       @Nullable LivingEntity owner, int seed,
                       ItemDisplayContext displayContext) {
        if (level == null) return false;
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        long last = data.copyTag().getLong("LastShot").orElse(0L);
        return (level.getDefaultClockTime() - last) < 2;
    }

    public MapCodec<IsFiring> type() {
        return MAP_CODEC;
    }
}