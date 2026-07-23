package bunger.group.tyler.structure;

import bunger.group.MutuallyAssuredDestruction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructures {

    public static StructurePieceType SQUIRREL_HOUSE_PIECE;
    public static StructureType<SquirrelHouseStructure> SQUIRREL_HOUSE_TYPE;

    public static void register() {
        SQUIRREL_HOUSE_PIECE = Registry.register(
                BuiltInRegistries.STRUCTURE_PIECE,
                Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "squirrel_house"),
                (ctx, tag) -> new SquirrelHousePiece(ctx.structureTemplateManager(), tag)
        );

    }
}