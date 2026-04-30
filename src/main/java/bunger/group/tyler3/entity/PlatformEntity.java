package bunger.group.tyler3.entity;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler3.block.PlatformTrackBlock;
import bunger.group.tyler3.item.StickyGooItem;
import bunger.group.tyler3.tools.EntityAttachmentData;
import bunger.group.tyler3.tools.EntityAttachmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlatformEntity extends Entity {

    private static final EntityDataAccessor<Boolean> DATA_IS_CARRIER =
            SynchedEntityData.defineId(PlatformEntity.class, EntityDataSerializers.BOOLEAN);

    // For followers only
    private UUID carrierUUID = null;
    private Vec3 offsetFromCarrier = Vec3.ZERO;

    // For carrier only
    private boolean movingForward = true;
    private boolean isMoving = false;
    private final EntityAttachmentData attachmentData = new EntityAttachmentData();

    // Carried block state (for followers — the block they represent)
    private BlockState carriedState = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();

    public static final double SPEED = 0.1;

    public PlatformEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_IS_CARRIER, true);
    }

    public boolean isCarrier() {
        return this.getEntityData().get(DATA_IS_CARRIER);
    }

    public void setIsCarrier(boolean carrier) {
        this.getEntityData().set(DATA_IS_CARRIER, carrier);
    }

    public EntityAttachmentData getAttachmentData() {
        return attachmentData;
    }

    public BlockState getCarriedState() { return carriedState; }
    public void setCarriedState(BlockState state) { this.carriedState = state; }

    public UUID getCarrierUUID() { return carrierUUID; }
    public void setCarrierUUID(UUID uuid) { this.carrierUUID = uuid; }

    public Vec3 getOffsetFromCarrier() { return offsetFromCarrier; }
    public void setOffsetFromCarrier(Vec3 offset) { this.offsetFromCarrier = offset; }
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return passenger.position(); // keep them where they are on dismount
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        // Place passenger on top of platform
        double y = this.getY() + this.getBbHeight();
        moveFunction.accept(passenger, this.getX(), y, this.getZ());
    }

    @Override
    public boolean canAddPassenger(Entity passenger) {
        return true;
    }
    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) return;
        if (!isCarrier()) return;

        ServerLevel serverLevel = (ServerLevel) level();

        // Start moving if not already
        if (!isMoving) {
            isMoving = true;
            EntityAttachmentHelper.buildFollowerTree(this, serverLevel);
            EntityAttachmentHelper.spawnFollowerEntities(this, serverLevel);
        }

        tickMovement(serverLevel);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    private void tickMovement(ServerLevel level) {
        BlockPos trackPos = findTrackBelow();
        if (trackPos == null) return;

        BlockState trackState = level.getBlockState(trackPos);
        RailShape shape = trackState.getValue(PlatformTrackBlock.SHAPE);

        var exits = EntityAttachmentHelper.getExitsForShape(shape);
        if (exits == null) return;

        BlockPos exitA = trackPos.offset(exits.getFirst());
        BlockPos exitB = trackPos.offset(exits.getSecond());
        BlockPos nextTrack = movingForward ? exitB : exitA;

        if (!(level.getBlockState(nextTrack).getBlock() instanceof PlatformTrackBlock)) {
            movingForward = !movingForward;
            nextTrack = movingForward ? exitB : exitA;
            if (!(level.getBlockState(nextTrack).getBlock() instanceof PlatformTrackBlock)) {
                return;
            }
        }

        Vec3 target = Vec3.atBottomCenterOf(nextTrack).add(0, 0.5, 0);
        Vec3 current = this.position();
        Vec3 delta = target.subtract(current);
        double dist = delta.length();

        Vec3 movement;
        boolean arriving = dist <= SPEED;
        if (arriving) {
            movement = delta;
        } else {
            movement = delta.normalize().scale(SPEED);
        }

        // Move platform
        this.setPos(current.add(movement));
        // In tickMovement, after setPos:
        AABB topSurface = new AABB(
                this.getX()-0.5, this.getY()+0.9, this.getZ()-0.5,
                this.getX()+0.5, this.getY()+1.1, this.getZ()+0.5
        );
        level.getEntitiesOfClass(Entity.class, topSurface,
                        e -> !(e instanceof PlatformEntity) && !(e instanceof Player && ((Player)e).isSpectator()))
                .forEach(e -> e.startRiding(this));
        crushOrPushEntitiesAhead(level, movement);
        // Move all followers
        moveFollowers(level, movement);

        // If we arrived at track centre, land followers
        if (arriving) {
            EntityAttachmentHelper.landFollowers(this, level);
            isMoving = false;
        }
    }
    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        ItemStack held = player.getItemInHand(hand);

        if (!(held.getItem() instanceof StickyGooItem)) {
            if (isCarrier() && !level().isClientSide()) {
                isMoving = !isMoving;
                if (isMoving) {
                    EntityAttachmentHelper.buildFollowerTree(this, (ServerLevel) level());
                    EntityAttachmentHelper.spawnFollowerEntities(this, (ServerLevel) level());
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!isCarrier()) return InteractionResult.PASS;
        if (level().isClientSide()) return InteractionResult.SUCCESS;

        Vec3 hitPos = location.subtract(this.position());
        Direction face = EntityAttachmentHelper.getFaceFromHit(hitPos, new Vec3(0.5, 0.5, 0.5));

        if (getAttachmentData().hasPlatformFace(face)) {
            return InteractionResult.FAIL;
        }

        boolean applied = EntityAttachmentHelper.applyGooToPlatformFace(this, face);
        if (applied) {
            held.shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


    private void moveFollowers(ServerLevel level, Vec3 movement) {
        List<PlatformEntity> followers = level.getEntitiesOfClass(
                PlatformEntity.class,
                this.getBoundingBox().inflate(64),
                e -> !e.isCarrier() && getUUID().equals(e.getCarrierUUID())
        );

        for (PlatformEntity follower : followers) {
            Vec3 newPos = follower.position().add(movement);
            BlockPos newBlockPos = BlockPos.containing(newPos);
            BlockPos currentBlockPos = BlockPos.containing(follower.position());

            // Only check collision when crossing into a new block
            if (!newBlockPos.equals(currentBlockPos)) {
                BlockState blockAtNew = level.getBlockState(newBlockPos);
                boolean obstructed = !blockAtNew.isAir()
                        && !(blockAtNew.getBlock() instanceof PlatformTrackBlock)
                        && !blockAtNew.getBlock().equals(follower.getCarriedState().getBlock());

                if (obstructed) {
                    handleFollowerCollision(follower, level);
                    continue;
                }
            }

            follower.setPos(newPos);
        }
    }

    private void handleFollowerCollision(PlatformEntity follower, ServerLevel level) {
        MutuallyAssuredDestruction.LOGGER.info(
                "[Platform] Follower collision at {} — detaching", follower.position());

        // Drop the carried block as an item
        BlockPos dropPos = BlockPos.containing(follower.position());
        follower.spawnAtLocation(level,
                follower.getCarriedState().getBlock().asItem().getDefaultInstance());

        // Drop goo items for each face this follower had goo on
        Vec3i offset = toVec3i(follower.getOffsetFromCarrier());
        FollowerNode node = attachmentData.getFollower(offset);
        if (node != null) {
            int gooCount = node.gooFaces.size();
            if (gooCount > 0) {
                follower.spawnAtLocation(level,
                        new net.minecraft.world.item.ItemStack(
                                bunger.group.tyler3.item.ModItems.STICKY_GOO, gooCount));
            }
            attachmentData.removeFollower(offset);
        }

        // Remove platform face goo if this was directly attached to platform
        for (Direction face : attachmentData.getPlatformFaces()) {
            Vec3i faceOffset = new Vec3i(
                    face.getStepX(), face.getStepY(), face.getStepZ());
            if (faceOffset.equals(offset)) {
                attachmentData.removePlatformFace(face);
                // Drop the goo that connected platform to this follower
                follower.spawnAtLocation(level,
                        new net.minecraft.world.item.ItemStack(
                                bunger.group.tyler3.item.ModItems.STICKY_GOO, 1));
                break;
            }
        }

        follower.discard();
    }

    private static Vec3i toVec3i(Vec3 v) {
        return new Vec3i(
                (int) Math.round(v.x),
                (int) Math.round(v.y),
                (int) Math.round(v.z)
        );
    }

    private BlockPos findTrackBelow() {
        BlockPos pos = this.blockPosition();
        if (level().getBlockState(pos).getBlock() instanceof PlatformTrackBlock) return pos;
        BlockPos below = pos.below();
        if (level().getBlockState(below).getBlock() instanceof PlatformTrackBlock) return below;
        return null;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setIsCarrier(input.getBooleanOr("IsCarrier", true));
        movingForward = input.getBooleanOr("MovingForward", true);
        isMoving = input.getBooleanOr("IsMoving", false);
        carriedState = input.read("CarriedState", BlockState.CODEC)
                .orElse(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
        if (input.getBooleanOr("HasCarrier", false)) {
            carrierUUID = input.read("CarrierUUID", net.minecraft.core.UUIDUtil.CODEC).orElse(null);
        }
        double ox = input.getDoubleOr("OffsetX", 0);
        double oy = input.getDoubleOr("OffsetY", 0);
        double oz = input.getDoubleOr("OffsetZ", 0);
        offsetFromCarrier = new Vec3(ox, oy, oz);
        input.read("AttachmentData", CompoundTag.CODEC)
                .ifPresent(tag -> {
                    EntityAttachmentData loaded = EntityAttachmentData.load(tag);
                    attachmentData.clear();
                    for (Direction face : loaded.getPlatformFaces()) {
                        attachmentData.addPlatformFace(face);
                    }
                    for (Map.Entry<Vec3i, FollowerNode> entry : loaded.getFollowers().entrySet()) {
                        attachmentData.addFollower(
                                entry.getKey(),
                                entry.getValue().state,
                                entry.getValue().gooFaces
                        );
                    }
                });
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putBoolean("IsCarrier", isCarrier());
        output.putBoolean("MovingForward", movingForward);
        output.putBoolean("IsMoving", isMoving);
        output.store("CarriedState", BlockState.CODEC, carriedState);
        if (carrierUUID != null) {
            output.putBoolean("HasCarrier", true);
            output.store("CarrierUUID", net.minecraft.core.UUIDUtil.CODEC, carrierUUID);
        }
        output.putDouble("OffsetX", offsetFromCarrier.x);
        output.putDouble("OffsetY", offsetFromCarrier.y);
        output.putDouble("OffsetZ", offsetFromCarrier.z);
        output.store("AttachmentData", CompoundTag.CODEC, attachmentData.save());
    }
    private void crushOrPushEntitiesAhead(ServerLevel level, Vec3 movement) {
        if (movement.lengthSqr() == 0) return;

        AABB pushBox = this.getBoundingBox().expandTowards(movement.scale(1.2));

        List<Entity> inPath = level.getEntities(this, pushBox, e ->
                !(e instanceof PlatformEntity) && !e.isSpectator()
        );

        Vec3 pushDir = movement.normalize();

        for (Entity entity : inPath) {
            Vec3 toEntity = entity.position().subtract(this.position());
            if (toEntity.dot(pushDir) <= 0) continue;

            // Try to push first
            if (entity.isPushable()) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(pushDir.scale(SPEED * 2)));
                entity.hurtMarked = true;
            }

            // Check if there's a solid block directly behind the entity in the push direction
            // — if so, they're pinned and should take crush damage
            BlockPos behindEntity = BlockPos.containing(
                    entity.position().add(pushDir.scale(0.6))
            );
            BlockState behind = level.getBlockState(behindEntity);
            boolean pinned = behind.isSolid() || behind.getBlock() instanceof PlatformTrackBlock;

            if (pinned) {
                entity.hurt(
                        level.damageSources().cramming(),
                        4.0f
                );
            }
        }
    }
    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(1.0f, 1.0f);
    }
    @Override public boolean isPickable() { return true; }
    @Override public boolean isPushable() { return false; }

    public ItemStack getPickResult() {
        return net.minecraft.world.item.Items.AIR.getDefaultInstance();
    }
}