// bunger/group/client/alex/item/ArbalestPull.java
package bunger.group.client.alex.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class ArbalestPull implements RangeSelectItemModelProperty {
    public static final MapCodec<ArbalestPull> MAP_CODEC = MapCodec.unit(new ArbalestPull());

    public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        LivingEntity entity = owner == null ? null : owner.asLivingEntity();
        if (entity == null) return 0.0F;
        if (CrossbowItem.isCharged(stack)) return 0.0F;
        return (float) UseDuration.useDuration(stack, entity) / 400.0F;
    }

    public MapCodec<ArbalestPull> type() {
        return MAP_CODEC;
    }
}