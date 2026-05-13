package bunger.group.alex.item.spell;

import bunger.group.alex.Mana;
import bunger.group.alex.entity.MageMob;
import bunger.group.alex.spell.SpellHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static bunger.group.alex.ParticleHelpers.spawnStaticParticle;
import static net.minecraft.world.phys.Vec3.atCenterOf;

public class SpellTemplate extends Item {

    int MANA_USE;
    public int RANGE;
    SpellTypes TYPE;

    public SpellTemplate(Properties properties, int manaUse, int range, SpellTypes type) {
        super(properties.stacksTo(1));
        properties.stacksTo(1);
        this.MANA_USE = manaUse;
        this.RANGE = range;
        this.TYPE = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.literal("Mana: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(this.MANA_USE)).withStyle(ChatFormatting.DARK_PURPLE)));
        builder.accept(Component.literal("Range: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(this.RANGE)).withStyle(ChatFormatting.GRAY)));
    }

    /**
     * Returns the cast target position if the user is a mob caster, otherwise null.
     * Subclasses should use this to determine aim direction instead of look angle
     * when the user is not a player.
     */
    @Nullable
    protected Vec3 getCastTarget(LivingEntity user) {
        if (user instanceof MageMob mage) {
            return mage.getCastTarget();
        }
        return null;
    }

    /**
     * Builds the end point for a raycast, respecting mob cast targets.
     * Use this in subclasses instead of manually reading look angle.
     */
    protected Vec3 getCastEndPoint(LivingEntity user) {
        Vec3 start = user.getEyePosition();
        Vec3 target = getCastTarget(user);
        if (target != null) {
            Vec3 dir = target.subtract(start).normalize();
            return start.add(dir.scale(this.RANGE));
        }
        return start.add(user.getLookAngle().scale(this.RANGE));
    }

    public void cast(Level level, LivingEntity user, ItemStack stack) {
        System.out.println("Some idiot forgot to @Override public void cast(Level level, LivingEntity user, ItemStack stack){}...");
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (Mana.get(user).getCurrentMana() >= this.MANA_USE) {
            Mana.get(user).useMana(this.MANA_USE);
            ItemStack stack = user.getItemInHand(hand);
            this.cast(level, user, stack);
            return InteractionResult.SUCCESS;
        }

        user.sendOverlayMessage(Component.literal("Not enough mana!"));
        return InteractionResult.PASS;
    }


    /**
     * Speed = step * 20/delay
     * @param level
     * @param user
     * @param dir
     * @param start
     * @param curDist
     * @param step
     * @param delay
     * @param particle
     */
    public void advanceBeam(Level level, LivingEntity user, Vec3 dir, Vec3 start, int curDist, int step, int delay, ParticleOptions particle) {
        if (curDist >= this.RANGE) return;

        SpellHelpers.runAfterTicks(delay, () -> {

            for (int j = 0; j < step; j++) { // one for each block
                Vec3 pos = start.add(dir.scale(curDist + j));
                Vec3 posNext = start.add(dir.scale(curDist + j + 1));

                BlockHitResult blockHit = level.clip(new ClipContext(
                        pos,
                        posNext,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        user
                ));

                if (blockHit.getType() == HitResult.Type.BLOCK) {
                    Vec3 hitPos = blockHit.getLocation();
                    spawnStaticParticle(level, hitPos, particle);
                    spawnStaticParticle(level, hitPos, particle);
                    impactResult(level, user, blockHit, null);
                    return;
                }

                // Entity collision
                AABB hitbox = new AABB(pos.x - 0.5, pos.y - 0.5, pos.z - 0.5,
                        pos.x + 0.5, pos.y + 0.5, pos.z + 0.5);
                LivingEntity hit = level.getEntitiesOfClass(LivingEntity.class, hitbox, e -> e != user)
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (hit != null) {
                    spawnStaticParticle(level, pos, particle);
                    spawnStaticParticle(level, pos, particle);
                    impactResult(level, user, null, hit);
                    return;
                }

                spawnStaticParticle(level, pos, particle);
                spawnStaticParticle(level, pos.add(dir.scale(0.5)), particle); // half block step to fill out beam
            }

            advanceBeam(level, user, dir, start, curDist+step, step, delay, particle);
        });
    }


    public void impactResult(Level level, LivingEntity user, @Nullable BlockHitResult blockHit, @Nullable LivingEntity entityHit)
    {
        if (blockHit != null) {
        }
        if (entityHit != null) {
        }
    }
}