package bunger.group.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.saveddata.SavedData;

public class StructureEventData extends SavedData {

    private static final String KEY = "structure_event_data";

    // event state
    private boolean everEntered = false;
    private int eventDay = 0;
    private long lastDayProcessed = -1;
    private boolean eventComplete = false;
    private boolean godSpawned = false;
    private boolean spawnpointLocked = false;
    private boolean doorLocked = false;
    public boolean hasPaintingPos() {
        return !paintingPos.equals(BlockPos.ZERO);
    }
    private BlockPos trueOrigin = BlockPos.ZERO;
    private Rotation structureRotation = Rotation.NONE;
    private net.minecraft.world.level.block.state.BlockState doorBottomState = null;
    private net.minecraft.world.level.block.state.BlockState doorTopState = null;

    // positions
    private BlockPos structureOrigin = BlockPos.ZERO;
    private BlockPos structureEnd = BlockPos.ZERO;
    private BlockPos bedPos = BlockPos.ZERO;
    private BlockPos paintingPos = BlockPos.ZERO;
    public int getPaintingIndex() { return eventDay; }
    public static StructureEventData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                StructureEventData::load,
                StructureEventData::new,
                KEY
        );
    }

    public static StructureEventData load(CompoundTag tag) {
        StructureEventData data = new StructureEventData();
        data.everEntered       = tag.getBoolean("everEntered");
        data.eventDay          = tag.getInt("eventDay");
        data.lastDayProcessed  = tag.getLong("lastDayProcessed");
        data.eventComplete     = tag.getBoolean("eventComplete");
        data.godSpawned        = tag.getBoolean("godSpawned");
        data.spawnpointLocked  = tag.getBoolean("spawnpointLocked");
        data.doorLocked = tag.getBoolean("doorLocked");
        data.trueOrigin = new BlockPos(
                tag.getInt("trueOriginX"), tag.getInt("trueOriginY"), tag.getInt("trueOriginZ"));
        data.structureRotation = Rotation.valueOf(
                tag.getString("structureRotation").isEmpty()
                        ? "NONE" : tag.getString("structureRotation"));

        data.structureOrigin   = new BlockPos(
                tag.getInt("originX"), tag.getInt("originY"), tag.getInt("originZ"));
        data.structureEnd      = new BlockPos(
                tag.getInt("endX"), tag.getInt("endY"), tag.getInt("endZ"));
        data.bedPos            = new BlockPos(
                tag.getInt("bedX"), tag.getInt("bedY"), tag.getInt("bedZ"));
        data.paintingPos       = new BlockPos(
                tag.getInt("paintingX"), tag.getInt("paintingY"), tag.getInt("paintingZ"));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("everEntered",      everEntered);
        tag.putInt("eventDay",             eventDay);
        tag.putLong("lastDayProcessed",    lastDayProcessed);
        tag.putBoolean("eventComplete",    eventComplete);
        tag.putBoolean("godSpawned",       godSpawned);
        tag.putBoolean("spawnpointLocked", spawnpointLocked);
        tag.putInt("originX",  structureOrigin.getX());
        tag.putInt("originY",  structureOrigin.getY());
        tag.putInt("originZ",  structureOrigin.getZ());
        tag.putInt("endX",     structureEnd.getX());
        tag.putInt("endY",     structureEnd.getY());
        tag.putInt("endZ",     structureEnd.getZ());
        tag.putInt("bedX",     bedPos.getX());
        tag.putInt("bedY",     bedPos.getY());
        tag.putInt("bedZ",     bedPos.getZ());
        tag.putInt("paintingX", paintingPos.getX());
        tag.putInt("paintingY", paintingPos.getY());
        tag.putInt("paintingZ", paintingPos.getZ());
        tag.putBoolean("doorLocked", doorLocked);
        tag.putString("structureRotation", structureRotation.name());
        tag.putInt("trueOriginX", trueOrigin.getX());
        tag.putInt("trueOriginY", trueOrigin.getY());
        tag.putInt("trueOriginZ", trueOrigin.getZ());
        return tag;
    }

    // getters
    public boolean hasBeenEntered()     { return everEntered; }
    public int getEventDay()            { return eventDay; }
    public long getLastDayProcessed()   { return lastDayProcessed; }
    public boolean isEventComplete()    { return eventComplete; }
    public boolean isGodSpawned()       { return godSpawned; }
    public boolean isSpawnpointLocked() { return spawnpointLocked; }
    public BlockPos getStructureOrigin(){ return structureOrigin; }
    public BlockPos getStructureEnd()   { return structureEnd; }
    public BlockPos getBedPos()         { return bedPos; }
    public BlockPos getPaintingPos()    { return paintingPos; }
    public boolean isDoorLocked() { return doorLocked; }
    public void setDoorLocked()   { doorLocked = true; setDirty(); }
    public void clearDoorLocked() { doorLocked = false; setDirty(); }
    public Rotation getStructureRotation() { return structureRotation; }
    public void setStructureRotation(Rotation r) { structureRotation = r; setDirty(); }
    public BlockPos getTrueOrigin()          { return trueOrigin; }
    public void setTrueOrigin(BlockPos pos)  { trueOrigin = pos; setDirty(); }

    // setters
    public void setEntered()            { everEntered = true; setDirty(); }
    public void setEventComplete()      { eventComplete = true; spawnpointLocked = false; setDirty(); }
    public void setGodSpawned()         { godSpawned = true; setDirty(); }
    public void setSpawnpointLocked()   { spawnpointLocked = true; setDirty(); }
    public void advanceDay(long day)    { eventDay++; lastDayProcessed = day; setDirty(); }
    public net.minecraft.world.level.block.state.BlockState getDoorBottomState() { return doorBottomState; }
    public net.minecraft.world.level.block.state.BlockState getDoorTopState()    { return doorTopState; }
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
        setDirty();
    }

    public void setBedPos(BlockPos pos)     { this.bedPos = pos; setDirty(); }
    public void setPaintingPos(BlockPos pos){ this.paintingPos = pos; setDirty(); }
}