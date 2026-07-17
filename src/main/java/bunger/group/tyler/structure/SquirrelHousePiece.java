package bunger.group.tyler.structure;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.event.TickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class SquirrelHousePiece extends TemplateStructurePiece {

    public SquirrelHousePiece(StructureTemplateManager manager, BlockPos pos) {
        super(ModStructures.SQUIRREL_HOUSE_PIECE,
                0, manager,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "squirrel_house"),
                "squirrel_house",
                new StructurePlaceSettings(),
                pos);
    }

    public SquirrelHousePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(ModStructures.SQUIRREL_HOUSE_PIECE,
                tag, manager,
                id -> new StructurePlaceSettings());
    }

    @Override
    protected void handleDataMarker(String marker, BlockPos pos,
                                    ServerLevelAccessor level,
                                    RandomSource random,
                                    BoundingBox box) {
        // not needed
    }

    @Override
    public void postProcess(WorldGenLevel level,
                            net.minecraft.world.level.StructureManager structureManager,
                            ChunkGenerator generator,
                            RandomSource random,
                            BoundingBox box,
                            ChunkPos chunkPos,
                            BlockPos pos) {
        super.postProcess(level, structureManager, generator,
                random, box, chunkPos, pos);

        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            BoundingBox bb = this.getBoundingBox();
            BlockPos origin = new BlockPos(bb.minX(), bb.minY(), bb.minZ());

            System.out.println("[SquirrelHouse] postProcess bb=" + bb + " origin=" + origin);

            // Defer to main thread — SavedData is not safe during chunk gen
            TickScheduler.schedule(serverLevel, serverLevel.getGameTime() + 1L, () -> {
                StructureEventData data = StructureEventData.get(serverLevel);
                if (!data.getStructureOrigin().equals(BlockPos.ZERO)) return; // already set

                StructurePlacer.savePositions(serverLevel, origin, Rotation.NONE);
                System.out.println("[SquirrelHouse] Saved origin=" + origin);
            });
        }
    }
}