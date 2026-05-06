package bunger.group.alex.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class GoblinGruntEntity extends Monster {

    public static final float SPEAR_CHANCE = 0.33f;
    public static final float AXE_CHANCE   = 0.33f;
    // Remaining ~0.34 = sword

    public static final float STONE_CHANCE = 0.50f;
    // Remaining 0.50 = iron

    public GoblinGruntEntity(EntityType<? extends GoblinGruntEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 50.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    private ItemStack pickTieredWeapon(ItemStack stone, ItemStack iron) {
        return this.random.nextFloat() < STONE_CHANCE ? stone : iron;
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, DifficultyInstance difficulty) {
        float weaponRoll = random.nextFloat();

        if (weaponRoll < SPEAR_CHANCE) {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_SPEAR),
                    new ItemStack(Items.IRON_SPEAR)
            ));
        } else if (weaponRoll < SPEAR_CHANCE + AXE_CHANCE) {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_AXE),
                    new ItemStack(Items.IRON_AXE)
            ));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, pickTieredWeapon(
                    new ItemStack(Items.STONE_SWORD),
                    new ItemStack(Items.IRON_SWORD)
            ));
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                  EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }
}