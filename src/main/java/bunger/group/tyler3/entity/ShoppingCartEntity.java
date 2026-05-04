package bunger.group.tyler3.entity;

import bunger.group.tyler3.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShoppingCartEntity extends AbstractMinecart {

    private static final EntityDataAccessor<Long> PUSHER_UUID_MOST =
            SynchedEntityData.defineId(ShoppingCartEntity.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Long> PUSHER_UUID_LEAST =
            SynchedEntityData.defineId(ShoppingCartEntity.class, EntityDataSerializers.LONG);

    private static final long NO_UUID = 0L;
    private static final float PUSH_DISTANCE = 1.3F;
    private Vec3 lastPushVelocity = Vec3.ZERO;
    // Higher lerp = cart tracks pusher more tightly each tick
    private static final double PUSH_LERP = 0.6;

    private static final double MOMENTUM_DECAY = 0.9999;
    private static final double STOP_THRESHOLD = 0.005;

    // Ticks an ejected entity is immune from re-scooping
    private static final int SCOOP_COOLDOWN_TICKS = 40;

    private static final EntityDimensions DIMENSIONS = EntityDimensions.scalable(1.8F, 0.9F);

    // Server-side only: tracks recently ejected entities so they can't be
    // immediately re-scooped (anti-cramming cooldown)
    private final Map<UUID, Integer> scoopCooldowns = new HashMap<>();

    public ShoppingCartEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    // -------------------------------------------------------------------------
    // SynchedEntityData
    // -------------------------------------------------------------------------

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PUSHER_UUID_MOST,  NO_UUID);
        builder.define(PUSHER_UUID_LEAST, NO_UUID);
    }

    // -------------------------------------------------------------------------
    // Dimensions
    // -------------------------------------------------------------------------

    @Override
    public EntityDimensions getDimensions(net.minecraft.world.entity.Pose pose) {
        return DIMENSIONS;
    }

    // -------------------------------------------------------------------------
    // Pusher UUID helpers
    // -------------------------------------------------------------------------

    @Nullable
    public UUID getPusherUUID() {
        long most  = this.entityData.get(PUSHER_UUID_MOST);
        long least = this.entityData.get(PUSHER_UUID_LEAST);
        if (most == NO_UUID && least == NO_UUID) return null;
        return new UUID(most, least);
    }

    public void setPusherUUID(@Nullable UUID uuid) {
        if (uuid == null) {
            this.entityData.set(PUSHER_UUID_MOST,  NO_UUID);
            this.entityData.set(PUSHER_UUID_LEAST, NO_UUID);
        } else {
            this.entityData.set(PUSHER_UUID_MOST,  uuid.getMostSignificantBits());
            this.entityData.set(PUSHER_UUID_LEAST, uuid.getLeastSignificantBits());
        }
    }

    public boolean isPushed() {
        return getPusherUUID() != null;
    }

    @Nullable
    private Player resolvePusher() {
        UUID uuid = getPusherUUID();
        if (uuid == null) return null;
        return this.level().getPlayerByUUID(uuid);
    }

    // -------------------------------------------------------------------------
    // Interaction
    // -------------------------------------------------------------------------

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 vec) {
        if (this.level().isClientSide()) return InteractionResult.SUCCESS;

        boolean hitHandle = vec.z < 0.4;

        if (hitHandle) {
            return handleInteract(player);
        } else {
            return basketInteract(player);
        }
    }

    private InteractionResult handleInteract(Player player) {
        UUID current = getPusherUUID();

        if (current != null && current.equals(player.getUUID())) {
            detachPusher();
            return InteractionResult.CONSUME;
        }

        if (current != null) return InteractionResult.PASS;

        setPusherUUID(player.getUUID());
        this.noPhysics = false;
        return InteractionResult.CONSUME;
    }

    private InteractionResult basketInteract(Player player) {
        if (isPushed()) return InteractionResult.PASS;
        if (player.isSecondaryUseActive()) return InteractionResult.PASS;
        if (!this.getPassengers().isEmpty()) return InteractionResult.PASS;
        player.startRiding(this);
        return InteractionResult.CONSUME;
    }

    // -------------------------------------------------------------------------
    // Tick
    // -------------------------------------------------------------------------

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            scoopCooldowns.entrySet().removeIf(e -> {
                int remaining = e.getValue() - 1;
                e.setValue(remaining);
                return remaining <= 0;
            });
        }

        if (isPushed()) {
            tickPushed();
        } else {
            tickCoasting();
        }
    }

    private void tickCoasting() {
        this.baseTick();

        Vec3 movement = this.getDeltaMovement();

        double horizSq = movement.horizontalDistanceSqr();
        if (horizSq < STOP_THRESHOLD * STOP_THRESHOLD) {
            this.setDeltaMovement(0, movement.y - 0.08, 0); // just gravity
            this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());
            return;
        }

        // Apply gravity
        double newY = this.onGround() ? 0 : movement.y - 0.08;

        // Decay horizontal momentum
        double newX = movement.x * MOMENTUM_DECAY;
        double newZ = movement.z * MOMENTUM_DECAY;

        this.setDeltaMovement(newX, newY, newZ);
        this.move(net.minecraft.world.entity.MoverType.SELF, this.getDeltaMovement());

        // After move, if we hit the ground zero out Y
        if (this.onGround()) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x,
                    0,
                    this.getDeltaMovement().z
            );
        }

        this.needsSync = true;
    }

    private void tickPushed() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }
        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        Player pusher = resolvePusher();

        if (pusher == null || !pusher.isAlive() || pusher.isRemoved()) {
            detachWithMomentum();
            return;
        }

        if (pusher.isShiftKeyDown()) {
            detachWithMomentum();
            return;
        }
        if (!pusher.onGround() && pusher.getDeltaMovement().y > 0.1) {
            detachWithMomentum();
            return;
        }

        this.baseTick();

        pusher.getAbilities().mayfly = false;

        Vec3 look = pusher.getLookAngle();
        Vec3 flatLook = new Vec3(look.x, 0, look.z);
        double len = flatLook.length();
        if (len > 1e-6) {
            flatLook = flatLook.scale(1.0 / len);
        } else {
            float yaw = pusher.getYRot() * ((float) Math.PI / 180F);
            flatLook = new Vec3(-Math.sin(yaw), 0, Math.cos(yaw));
        }

        Vec3 target  = pusher.position().add(flatLook.scale(PUSH_DISTANCE));
        Vec3 current = this.position();

        double nextX = current.x + (target.x - current.x) * PUSH_LERP;
        double nextZ = current.z + (target.z - current.z) * PUSH_LERP;

        // Track pusher Y directly — let the surface handle vertical clamping
        double nextY = pusher.getY();

        this.setPos(nextX, nextY, nextZ);
        this.setYRot(pusher.getYRot());
        this.setXRot(0.0F);

        double speed = this.getPassengers().isEmpty()
                ? (pusher.isSprinting() ? 0.30 : 0.15)
                : (pusher.isSprinting() ? 0.55 : 0.28);

        // Store horizontal velocity so it carries over on release
        Vec3 pushVec = flatLook.scale(speed);
        lastPushVelocity = pushVec; // save clean push velocity, no blending noise
        this.setDeltaMovement(flatLook.x * speed, this.getDeltaMovement().y, flatLook.z * speed);
        this.needsSync = true;

        scoopEntities(flatLook);
    }
    private void detachWithMomentum() {
        detachPusher();
        // Forcibly write the last known push velocity so super.tick() sees it
        Vec3 current = this.getDeltaMovement();
        this.setDeltaMovement(lastPushVelocity.x, current.y, lastPushVelocity.z);
    }
    private void scoopEntities(Vec3 flatLook) {
        if (this.level().isClientSide()) return;

        // Build a directional scoop box: push the base AABB outward in the
        // direction the cart is travelling so only entities directly in front
        // get grabbed, not ones the pusher is walking away from.
        AABB base = this.getBoundingBox();
        double reach = 0.35;
        AABB scoopBox = new AABB(
                base.minX + flatLook.x * reach - 0.15,
                base.minY,
                base.minZ + flatLook.z * reach - 0.15,
                base.maxX + flatLook.x * reach + 0.15,
                base.maxY,
                base.maxZ + flatLook.z * reach + 0.15
        );

        if (this.getPassengers().isEmpty()) {
            List<LivingEntity> nearby = this.level().getEntitiesOfClass(
                    LivingEntity.class,
                    scoopBox,
                    entity -> !entity.is(this)
                            && !entity.isPassenger()
                            && !(entity instanceof Player)
                            && entity.isAlive()
                            && !scoopCooldowns.containsKey(entity.getUUID())
            );

            if (!nearby.isEmpty()) {
                nearby.get(0).startRiding(this);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Eject with cooldown so the entity can't be immediately re-scooped
    // -------------------------------------------------------------------------

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide()) {
            scoopCooldowns.put(passenger.getUUID(), SCOOP_COOLDOWN_TICKS);
        }
    }

    public void detachPusher() {
        setPusherUUID(null);
        this.setNoGravity(false);
        this.noPhysics = false;
    }

    // -------------------------------------------------------------------------
    // Passenger positioning
    // -------------------------------------------------------------------------

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0.0, 0.3, 0.0);
    }

    // -------------------------------------------------------------------------
    // NBT
    // -------------------------------------------------------------------------

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        UUID pusher = getPusherUUID();
        if (pusher != null) {
            output.putLong("PusherUUIDMost",  pusher.getMostSignificantBits());
            output.putLong("PusherUUIDLeast", pusher.getLeastSignificantBits());
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        long most  = input.getLongOr("PusherUUIDMost",  NO_UUID);
        long least = input.getLongOr("PusherUUIDLeast", NO_UUID);
        if (most != NO_UUID || least != NO_UUID) {
            setPusherUUID(new UUID(most, least));
            this.noPhysics = false;
        }
    }

    // -------------------------------------------------------------------------
    // AbstractMinecart boilerplate
    // -------------------------------------------------------------------------

    @Override
    protected Item getDropItem() {
        return ModItems.SHOPPING_CART;
    }

    @Override
    public boolean isRideable() {
        return true;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ModItems.SHOPPING_CART);
    }
}