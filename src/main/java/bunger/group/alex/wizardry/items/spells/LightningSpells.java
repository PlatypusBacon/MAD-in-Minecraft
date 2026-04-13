package bunger.group.alex.wizardry.items.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import bunger.group.alex.wizardry.ParticleHelpers;
import bunger.group.alex.wizardry.SpellHelpers;

public class LightningSpells {

    public static final ScrollItem ZAP = new ScrollItem(new Item.Properties()) {
        @Override
        protected void cast(Level level, Player player, ItemStack stack) {
            // place water at block

            double range = 25.0;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(range));

            AABB box = new AABB(start, end).inflate(1.0);

            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    box,
                    entity -> entity instanceof LivingEntity && entity != player
            );

            if (entityHit != null) {
                Entity entity = entityHit.getEntity();
                float damageAmount = 1.0f;

                entity.hurt(DamageSource.MAGIC, damageAmount);
                end = entityHit.getLocation();
            }

            ParticleHelpers.spawnBeamParticles(level, start, end, ParticleTypes.ELECTRIC_SPARK);
        }
    };

    public static final ScrollItem LIGHTNING = new ScrollItem(new Item.Properties()) {

        @Override
        protected void cast(Level level, Player player, ItemStack stack) {
            // place water at block

            double range = 25.0;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(range));

            BlockHitResult blockHit = level.clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            AABB box = new AABB(start, end).inflate(1.0);

            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    box,
                    entity -> entity instanceof LivingEntity && entity != player
            );

            BlockPos pos = null;
            if (blockHit.getType() != HitResult.Type.MISS) {
                pos = blockHit.getBlockPos();
            }
            if (entityHit != null) {
                Entity entity = entityHit.getEntity();
                pos = entity.getOnPos();
            }

            if (pos != null) {
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);

                if (lightning != null) {
                    lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntity(lightning);
                }
            }
        }
    };

    public static final ScrollItem CHANNEL_STORM = new ScrollItem(new Item.Properties()) {

        @Override
        protected void cast(Level level, Player player, ItemStack stack) {

            double range = 40.0;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(range));

            BlockHitResult blockHit = level.clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            AABB box = new AABB(start, end).inflate(1.0);

            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    box,
                    entity -> entity instanceof LivingEntity && entity != player
            );

            Vec3 centre = null;
            if (blockHit.getType() != HitResult.Type.MISS) {
                BlockPos pos = blockHit.getBlockPos();
                centre = Vec3.atCenterOf(pos).add(0, 0.6, 0);
            }
            if (entityHit != null) {
                Entity entity = entityHit.getEntity();
                centre = entity.position().add(0, 0.1, 0);
            }

            if (centre != null) {
                Vec3 finalCentre = centre;
                ParticleHelpers.runForTicks(200, new Runnable() {
                    double offset = 0;
                    @Override
                    public void run() {
                        offset += 0.1;
                        ParticleHelpers.spawnRingParticles(level, finalCentre, 5.0,
                                offset, ParticleTypes.ELECTRIC_SPARK);
                    }
                });
            }

            if (centre != null) {
                double radius = 5.0;
                Vec3 finalCentre1 = centre;
                SpellHelpers.runForTicks(200, new Runnable() {
                    int tick = 0;

                    @Override
                    public void run() {
                        tick++;

                        if (tick % 8 == 0) {

                            // random point in circle
                            double angle = Math.random() * Math.PI * 2;
                            double dist = Math.sqrt(Math.random()) * radius;

                            double offsetX = Math.cos(angle) * dist;
                            double offsetZ = Math.sin(angle) * dist;

                            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);

                            if (lightning != null) {
                                lightning.moveTo(
                                        finalCentre1.x + offsetX,
                                        finalCentre1.y,
                                        finalCentre1.z + offsetZ
                                );
                                level.addFreshEntity(lightning);
                            }
                        }
                    }
                });
            }
        }
    };


    public static final ScrollItem AGARTHAN_THUNDER = new ScrollItem(new Item.Properties()) {

        @Override
        protected void cast(Level level, Player player, ItemStack stack) {

            double range = 40.0;
            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            Vec3 end = start.add(look.scale(range));

            BlockHitResult blockHit = level.clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.NONE,
                    player
            ));

            AABB box = new AABB(start, end).inflate(1.0);

            EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                    level,
                    player,
                    start,
                    end,
                    box,
                    entity -> entity instanceof LivingEntity && entity != player
            );

            Vec3 centre = null;
            if (blockHit.getType() != HitResult.Type.MISS) {
                BlockPos pos = blockHit.getBlockPos();
                centre = Vec3.atCenterOf(pos).add(0, 0.6, 0);
            }
            if (entityHit != null) {
                Entity entity = entityHit.getEntity();
                centre = entity.position().add(0, 0.1, 0);
            }

            if (centre != null) {
                Vec3 finalCentre = centre;
                ParticleHelpers.runForTicks(200, new Runnable() {
                    double offset = 0;
                    @Override
                    public void run() {
                        offset += 0.1;
                        ParticleHelpers.spawnRingParticles(level, finalCentre, 20,
                                offset, ParticleTypes.ELECTRIC_SPARK);
                    }
                });
            }

            if (centre != null) {
                double radius = 20.0;
                Vec3 finalCentre1 = centre;
                SpellHelpers.runForTicks(200, () -> {
                    for (int i = 0; i < 5; i++) {
                        double angle = Math.random() * Math.PI * 2;
                        double dist = Math.sqrt(Math.random()) * radius;

                        double offsetX = Math.cos(angle) * dist;
                        double offsetZ = Math.sin(angle) * dist;

                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);

                        if (lightning != null) {
                            lightning.moveTo(
                                    finalCentre1.x + offsetX,
                                    finalCentre1.y,
                                    finalCentre1.z + offsetZ
                            );
                            level.addFreshEntity(lightning);
                        }
                    }

                });
            }

        }
    };

    public static void create_item(String name, ScrollItem spell) {
        Registry.register(
            Registry.ITEM,
            new ResourceLocation("mutually-assured-destruction", name),
            spell);
    }

    public static void register() {
        create_item("spell_lightning_zap", ZAP);
        create_item("spell_lightning_lightning", LIGHTNING);
        create_item("spell_lightning_channel_storm", CHANNEL_STORM);
        create_item("spell_lightning_agarthan_thunder", AGARTHAN_THUNDER);
    }

}
