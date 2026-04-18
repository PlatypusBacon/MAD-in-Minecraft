package bunger.group.tyler.structure;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler.data.StructureEventData;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Optional;

public class SquirrelHouseStructure extends Structure {

    public static final MapCodec<SquirrelHouseStructure> CODEC =
            simpleCodec(SquirrelHouseStructure::new);

    public SquirrelHouseStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
        System.out.println("SquirrelHouse attempting generation at chunk: "
                + ctx.chunkPos());
        return onTopOfChunkCenter(ctx,
                Heightmap.Types.WORLD_SURFACE_WG,
                (builder) -> generatePieces(builder, ctx));
    }

    private void generatePieces(StructurePiecesBuilder builder,
                                GenerationContext ctx) {
        BlockPos pos = new BlockPos(
                ctx.chunkPos().getMiddleBlockX(),
                0, // height is handled by onTopOfChunkCenter
                ctx.chunkPos().getMiddleBlockZ()
        );

        builder.addPiece(new SquirrelHousePiece(
                ctx.structureTemplateManager(), pos));
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.SQUIRREL_HOUSE_TYPE;
    }
}