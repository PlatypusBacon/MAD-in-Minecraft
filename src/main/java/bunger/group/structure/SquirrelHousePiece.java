package bunger.group.structure;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.data.StructureEventData;
import bunger.group.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class SquirrelHousePiece extends TemplateStructurePiece {

    public SquirrelHousePiece(StructureTemplateManager manager, BlockPos pos) {
        super(ModStructures.SQUIRREL_HOUSE_PIECE,
                0, manager,
                new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "squirrel_house"),
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
                                    RandomSource random,  // fixed type
                                    BoundingBox box) {
        // not needed
    }
    @Override
    public void postProcess(net.minecraft.world.level.WorldGenLevel level,
                            net.minecraft.world.level.StructureManager structureManager,
                            net.minecraft.world.level.chunk.ChunkGenerator generator,
                            RandomSource random,
                            BoundingBox box,
                            net.minecraft.world.level.ChunkPos chunkPos,
                            BlockPos pos) {
        super.postProcess(level, structureManager, generator,
                random, box, chunkPos, pos);

        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            // use bounding box min as origin — more reliable than templatePosition
            BlockPos origin = new BlockPos(
                    box.minX(), box.minY(), box.minZ());
            System.out.println("Structure generated naturally at: " + origin);
            StructurePlacer.savePositions(serverLevel, origin);
        }
    }
}