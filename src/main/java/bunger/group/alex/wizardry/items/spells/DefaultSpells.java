package bunger.group.alex.wizardry.items.spells;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.damagesource.DamageSource;

public class DefaultSpells {

    public static final Item BASIC_SCROLL = Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", "basic_scroll"),
            new ScrollItem(new Item.Properties())
    );

    public static void register() {}
}