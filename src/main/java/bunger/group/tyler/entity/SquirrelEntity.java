package bunger.group.tyler.entity;

import bunger.group.tyler.enchantment.ModEnchantments;
import bunger.group.tyler.item.ModItems;
import bunger.group.tyler.sound.ModSounds;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SquirrelEntity extends Animal {

    public SquirrelEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        // Panic sprint if player is within 10 blocks
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 10f, 2.8, 3.5));
        // General avoidance from further away
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 24f, 1.8, 2.6));
        // Flee from anything that hurt them
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, LivingEntity.class, 10f, 2.2, 3.0));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }
    public static AttributeSupplier.Builder createCubeAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.45)  // base speed (multiplied at runtime)
                .add(Attributes.FOLLOW_RANGE, 36.0);
    }
    @Override
    protected void dropAllDeathLoot(net.minecraft.server.level.ServerLevel level, net.minecraft.world.damagesource.DamageSource source) {
        System.out.println("[SquirrelEntity] dropAllDeathLoot called");
        super.dropAllDeathLoot(level, source);
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
        super.dropCustomDeathLoot(level, source, killedByPlayer);
        if (this.random.nextFloat() < 0.7f) {
            net.minecraft.world.item.Item[] drops = {
                    ModItems.SQUEATHER_HEAD,
                    ModItems.SQUEATHER_CHEST,
                    ModItems.SQUEATHER_LEGS,
                    ModItems.SQUEATHER_FEET
            };
            net.minecraft.world.item.Item chosen = drops[this.random.nextInt(drops.length)];
            ItemStack stack = new ItemStack(chosen);

            // Apply the stapeled enchantment
            Registry<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            registry.get(ModEnchantments.STAPELED).ifPresent(enchantmentHolder -> {
                ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(
                        stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                );
                mutable.set(enchantmentHolder, 1);
                stack.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
            });

            this.spawnAtLocation(level, stack);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.SQUIRREL_AMBIENT;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }
}