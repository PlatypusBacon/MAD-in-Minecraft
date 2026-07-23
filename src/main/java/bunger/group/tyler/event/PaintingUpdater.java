package bunger.group.tyler.event;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class PaintingUpdater {

    private static final Identifier[] VARIANTS = {
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "god_coming_0"),
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "god_coming_1"),
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "god_coming_2"),
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "god_coming_3"),
            Identifier.fromNamespaceAndPath("mutually-assured-destruction", "god_coming_4"),
    };
    public static void updatePaintingToIndex(ServerLevel level, StructureEventData data, int index) {
        int clampedIndex = Math.max(0, Math.min(index, VARIANTS.length - 1));
        applyVariant(level, data, VARIANTS[clampedIndex]);
    }

    public static void updatePainting(ServerLevel level, StructureEventData data) {
        int nextIndex = (data.getPaintingIndex() + 1) % VARIANTS.length;
        updatePaintingToIndex(level, data, nextIndex);
    }

    private static void applyVariant(ServerLevel level, StructureEventData data, Identifier variantId) {
        BlockPos wallPos = StructurePlacer.getPaintingWallPos(level);
        Direction facing = StructurePlacer.getPaintingFacing(level);

        var registry = level.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT);
        var variantHolder = registry.get(ResourceKey.create(Registries.PAINTING_VARIANT, variantId));
        if (variantHolder.isEmpty()) {
            System.err.println("Painting variant not found: " + variantId);
            return;
        }

        // wipe anything at the canonical spot first — covers strays/duplicates too,
        // instead of relying on a radius search that can miss if anything is off
        level.getEntitiesOfClass(Painting.class, new AABB(wallPos).inflate(3.0))
                .forEach(p -> p.discard());

        Painting newPainting = new Painting(level, wallPos, facing, variantHolder.get());
        level.addFreshEntity(newPainting);
        data.setPaintingPos(wallPos.relative(facing));
        System.out.println("Painting updated to: " + variantId + " at " + wallPos);
    }
}