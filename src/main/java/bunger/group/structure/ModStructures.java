package bunger.group.structure;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructures {

    public static StructurePieceType SQUIRREL_HOUSE_PIECE;
    public static StructureType<SquirrelHouseStructure> SQUIRREL_HOUSE_TYPE;

    public static void register() {
        SQUIRREL_HOUSE_PIECE = Registry.register(
                Registry.STRUCTURE_PIECE,
                new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "squirrel_house"),
                (ctx, tag) -> new SquirrelHousePiece(ctx.structureTemplateManager(), tag)
        );

        SQUIRREL_HOUSE_TYPE = Registry.register(
                Registry.STRUCTURE_TYPES,  // fixed — no S
                new ResourceLocation(MutuallyAssuredDestruction.MOD_ID, "squirrel_house"),
                () -> SquirrelHouseStructure.CODEC
        );
    }
}