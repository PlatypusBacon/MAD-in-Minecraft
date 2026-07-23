package bunger.group.tyler.data;

import bunger.group.MutuallyAssuredDestruction;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class StructureEventData extends SavedData {

    // event state
    private boolean everEntered;
    private int eventDay;
    private long lastDayProcessed;
    private boolean eventComplete;
    private boolean godSpawned;
    private boolean spawnpointLocked;
    private boolean doorLocked;
    private BlockPos trueOrigin;
    private Rotation structureRotation;
    private net.minecraft.world.level.block.state.BlockState doorBottomState = null;
    private net.minecraft.world.level.block.state.BlockState doorTopState = null;
    private java.util.List<java.util.UUID> eventPlayers = new java.util.ArrayList<>();
    private boolean wifeEventTriggered = false;

    public boolean isWifeEventTriggered() { return wifeEventTriggered; }
    public void setWifeEventTriggered() { wifeEventTriggered = true; setDirty(); }

    // positions
    private BlockPos structureOrigin;
    private BlockPos structureEnd;
    private BlockPos bedPos;
    private BlockPos paintingPos;

    // Codec for serialization
    public static final Codec<StructureEventData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("everEntered", false)
                    .forGetter(d -> d.everEntered),
            Codec.INT.optionalFieldOf("eventDay", 0)
                    .forGetter(d -> d.eventDay),
            Codec.LONG.optionalFieldOf("lastDayProcessed", -1L)
                    .forGetter(d -> d.lastDayProcessed),
            Codec.BOOL.optionalFieldOf("eventComplete", false)
                    .forGetter(d -> d.eventComplete),
            Codec.BOOL.optionalFieldOf("godSpawned", false)
                    .forGetter(d -> d.godSpawned),
            Codec.BOOL.optionalFieldOf("spawnpointLocked", false)
                    .forGetter(d -> d.spawnpointLocked),
            Codec.BOOL.optionalFieldOf("doorLocked", false)
                    .forGetter(d -> d.doorLocked),
            Codec.list(UUIDUtil.CODEC).optionalFieldOf("eventPlayers", new java.util.ArrayList<>())
                    .forGetter(d -> d.eventPlayers),
            BlockPos.CODEC.optionalFieldOf("trueOrigin", BlockPos.ZERO)
                    .forGetter(d -> d.trueOrigin),
            Rotation.CODEC.optionalFieldOf("structureRotation", Rotation.NONE)
                    .forGetter(d -> d.structureRotation),
            BlockPos.CODEC.optionalFieldOf("structureOrigin", BlockPos.ZERO)
                    .forGetter(d -> d.structureOrigin),
            BlockPos.CODEC.optionalFieldOf("structureEnd", BlockPos.ZERO)
                    .forGetter(d -> d.structureEnd),
            BlockPos.CODEC.optionalFieldOf("bedPos", BlockPos.ZERO)
                    .forGetter(d -> d.bedPos),
            BlockPos.CODEC.optionalFieldOf("paintingPos", BlockPos.ZERO)
                    .forGetter(d -> d.paintingPos)
    ).apply(instance, StructureEventData::new));

    public static final SavedDataType<StructureEventData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "structure_event_data"),
            StructureEventData::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    // No-arg constructor (used by SavedDataType constructor supplier)
    public StructureEventData() {
        this.everEntered = false;
        this.eventDay = 0;
        this.lastDayProcessed = -1L;
        this.eventComplete = false;
        this.godSpawned = false;
        this.spawnpointLocked = false;
        this.doorLocked = false;
        this.trueOrigin = BlockPos.ZERO;
        this.structureRotation = Rotation.NONE;
        this.structureOrigin = BlockPos.ZERO;
        this.structureEnd = BlockPos.ZERO;
        this.bedPos = BlockPos.ZERO;
        this.paintingPos = BlockPos.ZERO;
    }

    private StructureEventData(boolean everEntered, int eventDay, long lastDayProcessed,
                               boolean eventComplete, boolean godSpawned, boolean spawnpointLocked,
                               boolean doorLocked, java.util.List<java.util.UUID> eventPlayers,
                               BlockPos trueOrigin, Rotation structureRotation,
                               BlockPos structureOrigin, BlockPos structureEnd,
                               BlockPos bedPos, BlockPos paintingPos) {
        this.everEntered = everEntered;
        this.eventDay = eventDay;
        this.lastDayProcessed = lastDayProcessed;
        this.eventComplete = eventComplete;
        this.godSpawned = godSpawned;
        this.spawnpointLocked = spawnpointLocked;
        this.doorLocked = doorLocked;
        this.eventPlayers = new java.util.ArrayList<>(eventPlayers);
        this.trueOrigin = trueOrigin;
        this.structureRotation = structureRotation;
        this.structureOrigin = structureOrigin;
        this.structureEnd = structureEnd;
        this.bedPos = bedPos;
        this.paintingPos = paintingPos;
    }

    public static StructureEventData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    // getters
    public boolean hasBeenEntered()     { return everEntered; }
    public int getEventDay()            { return eventDay; }
    public int getPaintingIndex()       { return eventDay; }
    public long getLastDayProcessed()   { return lastDayProcessed; }
    public boolean isEventComplete()    { return eventComplete; }
    public boolean isGodSpawned()       { return godSpawned; }
    public boolean isSpawnpointLocked() { return spawnpointLocked; }
    public boolean isDoorLocked()       { return doorLocked; }
    public BlockPos getStructureOrigin(){ return structureOrigin; }
    public BlockPos getStructureEnd()   { return structureEnd; }
    public BlockPos getBedPos()         { return bedPos; }
    public BlockPos getPaintingPos()    { return paintingPos; }
    public boolean hasPaintingPos()     { return !paintingPos.equals(BlockPos.ZERO); }
    public Rotation getStructureRotation()           { return structureRotation; }
    public BlockPos getTrueOrigin()                  { return trueOrigin; }
    public net.minecraft.world.level.block.state.BlockState getDoorBottomState() { return doorBottomState; }
    public net.minecraft.world.level.block.state.BlockState getDoorTopState()    { return doorTopState; }

    // setters
    public void setEntered()            { everEntered = true; setDirty(); }
    public void setEventComplete() {
        eventComplete = true;
        spawnpointLocked = false;
        eventPlayers.clear();
        setDirty();
    }
    public void setGodSpawned()         { godSpawned = true; setDirty(); }
    public void setSpawnpointLocked()   { spawnpointLocked = true; setDirty(); }
    public void advanceDay(long day)    { eventDay++; lastDayProcessed = day; setDirty(); }
    public void setDoorLocked()         { doorLocked = true; setDirty(); }
    public void clearDoorLocked()       { doorLocked = false; setDirty(); }
    public void setStructureRotation(Rotation r)     { structureRotation = r; setDirty(); }
    public void setTrueOrigin(BlockPos pos)          { trueOrigin = pos; setDirty(); }
    public void setBedPos(BlockPos pos)              { bedPos = pos; setDirty(); }
    public void setPaintingPos(BlockPos pos)         { paintingPos = pos; setDirty(); }
    public void setDoorBottomState(net.minecraft.world.level.block.state.BlockState state) { doorBottomState = state; }
    public void setDoorTopState(net.minecraft.world.level.block.state.BlockState state)    { doorTopState = state; }

    public void setStructureBounds(BlockPos origin, BlockPos end) {
        this.structureOrigin = origin;
        this.structureEnd = end;
        setDirty();
    }

    public void reset() {
        this.everEntered      = false;
        this.eventDay         = 0;
        this.lastDayProcessed = -1;
        this.eventComplete    = false;
        this.godSpawned       = false;
        this.spawnpointLocked = false;
        this.eventPlayers = new java.util.ArrayList<>();
        setDirty();
    }
    public void addEventPlayer(java.util.UUID uuid) {
        if (!eventPlayers.contains(uuid)) {
            eventPlayers.add(uuid);
            setDirty();
        }
    }
    public boolean isEventPlayer(java.util.UUID uuid) {
        return eventPlayers.contains(uuid);
    }

    public java.util.UUID getPrimaryEventPlayer() {
        return eventPlayers.isEmpty() ? null : eventPlayers.get(0);
    }
}