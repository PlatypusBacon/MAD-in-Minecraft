package bunger.group.tyler3.entity;

import bunger.group.tyler2.block.ModBlocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RockProjectileEntity extends ThrowableItemProjectile {

    public RockProjectileEntity(EntityType<? extends RockProjectileEntity> type, Level level) {
        super(type, level);
    }

    public RockProjectileEntity(EntityType<? extends RockProjectileEntity> type, LivingEntity shooter, Level level, ItemStack itemStack) {
        super(type, shooter, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModBlocks.ROCK.asItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        hitResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 3.0F);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }
}