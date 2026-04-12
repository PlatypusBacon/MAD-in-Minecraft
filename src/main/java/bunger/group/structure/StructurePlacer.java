package bunger.group.structure;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.data.StructureEventData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class StructurePlacer {

    // size of your structure in blocks — update these once you know them
    private static final int STRUCTURE_WIDTH  = 18;
    private static final int STRUCTURE_HEIGHT = 7;
    private static final int STRUCTURE_DEPTH  = 29;

    // offsets from structure origin to key positions
    // follow the steps below to find the correct values
    private static final int BED_OFFSET_X      = 19;
    private static final int BED_OFFSET_Y      = 1;
    private static final int BED_OFFSET_Z      = 6;
    private static final int PAINTING_OFFSET_X = 8;
    private static final int PAINTING_OFFSET_Y = 4;
    private static final int PAINTING_OFFSET_Z = 23;

    public static void checkAndSaveNaturalGeneration(ServerLevel level) {
        // find the squirrel house structure in the world
        var structureManager = level.structureManager();
        ResourceLocation structureId = new ResourceLocation(
                MutuallyAssuredDestruction.MOD_ID, "squirrel_house");

        // check near each player
        for (ServerPlayer player : level.players()) {
            BlockPos playerPos = player.blockPosition();

            // search in a 200 block radius around the player
            var structureStart = structureManager.getStructureWithPieceAt(
                    playerPos,
                    level.registryAccess()
                            .registryOrThrow(Registry.STRUCTURE_REGISTRY)
                            .getOrThrow(ResourceKey.create(
                                    Registry.STRUCTURE_REGISTRY, structureId))
            );

            if (structureStart != null && structureStart.isValid()) {
                BlockPos origin = new BlockPos(
                        structureStart.getBoundingBox().minX(),
                        structureStart.getBoundingBox().minY(),
                        structureStart.getBoundingBox().minZ()
                );

                StructureEventData data = StructureEventData.get(level);

                // only save if not already saved
                if (data.getStructureOrigin().equals(BlockPos.ZERO)) {
                    System.out.println("Found natural squirrel house at: " + origin);
                    savePositions(level, origin);
                }
                return;
            }
        }
    }

    public static void savePositions(ServerLevel level, BlockPos origin) {
        StructureEventData data = StructureEventData.get(level);
        data.setStructureBounds(
                origin,
                origin.offset(STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH)
        );
        data.setBedPos(
                origin.offset(BED_OFFSET_X, BED_OFFSET_Y, BED_OFFSET_Z)
        );
        data.setPaintingPos(
                origin.offset(PAINTING_OFFSET_X, PAINTING_OFFSET_Y, PAINTING_OFFSET_Z)
        );
        System.out.println("Structure positions saved at: " + origin);
    }
    public static void place(ServerLevel level, BlockPos pos, String structureName) {
        StructureTemplateManager manager = level.getStructureManager();

        ResourceLocation id = new ResourceLocation(
                MutuallyAssuredDestruction.MOD_ID, structureName);

        Optional<StructureTemplate> template = manager.get(id);

        if (template.isEmpty()) {
            System.err.println("Structure not found: " + id);
            return;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings();

        template.get().placeInWorld(
                level,
                pos,
                pos,
                settings,
                level.random,
                2
        );
        template.get().placeInWorld(level, pos, pos, settings, level.random, 2);
        savePositions(level, pos); // replace the manual setters with this
    }
}