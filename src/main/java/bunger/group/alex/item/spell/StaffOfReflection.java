package bunger.group.alex.item.spell;

import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static bunger.group.alex.ParticleHelpers.spawnStaticParticle;

public class StaffOfReflection extends SpellTemplate {
    public StaffOfReflection(Properties properties) {
        super(properties.useCooldown(60.0f), 100, 2, SpellTypes.STAFF);
    }

    @Override
    public void cast(Level level, LivingEntity user, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }

        final int range = this.RANGE;
        Set<Projectile> reflected = new HashSet<>();

        int[] tickCounter = {0};

        SpellHelpers.runForTicks(400, () -> {
            Vec3 userPos = user.position();

            AABB hitbox = new AABB(userPos.x - range, userPos.y - range, userPos.z - range,
                    userPos.x + range, userPos.y + range, userPos.z + range);
            List<Entity> hits = level.getEntitiesOfClass(Entity.class, hitbox, e -> e != user);

            for (Entity entity : hits) {
                if (entity instanceof Projectile projectile
                        && projectile.getOwner() != user
                        && reflected.add(projectile)) {
                    spawnStaticParticle(level, entity.position(), ParticleTypes.CRIT);
                    projectile.setDeltaMovement(projectile.getDeltaMovement().reverse().scale(1.1f));
                    projectile.setOwner(user);
                    projectile.hurtMarked = true;
                }
            }

            tickCounter[0]++;
            if (tickCounter[0] % 4 == 0) {
                ParticleHelpers.spawnRingParticles(level, userPos.add(0, 0.2, 0), 1.5,
                        0, ParticleTypes.CRIT);
                ParticleHelpers.spawnRingParticles(level, userPos.add(0, 2.5, 0), 1.5,
                        0, ParticleTypes.CRIT);
            }
        });
    }
}

