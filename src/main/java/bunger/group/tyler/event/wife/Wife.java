package bunger.group.tyler.event.wife;

import bunger.group.tyler.entity.SquirrelWifeEntity;
import bunger.group.tyler.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;

public class Wife {
    public static void start(ServerLevel level, SquirrelWifeEntity wife) {
        net.minecraft.world.item.Item drop = ModItems.SQUIRREL_STAPELER;
        ItemStack stack = new ItemStack(drop);
        wife.spawnAtLocation(level, stack);
        DamageSource source = level.damageSources().lightningBolt();
        wife.agghh(level, source);
        System.out.println("Wife Time baby");
    }
}
