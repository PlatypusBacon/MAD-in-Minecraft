package bunger.group.event;

import bunger.group.data.StructureEventData;
import bunger.group.structure.StructurePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

public class PaintingUpdater {

    private static final ResourceLocation[] VARIANTS = {
            new ResourceLocation("mutually-assured-destruction", "god_coming_0"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_1"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_2"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_3"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_4"),
    };

    public static void updatePaintingToIndex(ServerLevel level,
                                             StructureEventData data,
                                             int index) {
        // always resolve lazily in case it wasn't found at scan time
        BlockPos paintingPos = StructurePlacer.resolvePaintingPos(level, data);
        int clampedIndex = Math.max(0, Math.min(index, VARIANTS.length - 1));
        applyVariant(level, data, paintingPos, VARIANTS[clampedIndex]);
    }

    public static void updatePainting(ServerLevel level, StructureEventData data) {
        int nextIndex = (data.getPaintingIndex() + 1) % VARIANTS.length;
        updatePaintingToIndex(level, data, nextIndex);
    }

    private static void applyVariant(ServerLevel level,
                                     StructureEventData data,
                                     BlockPos searchPos,
                                     ResourceLocation variantId) {
        // search near stored pos first
        Painting painting = findNearest(level, searchPos, 3.0);

        // if not found there, search entire structure bounds as fallback
        if (painting == null) {
            System.err.println("Painting not near stored pos " + searchPos
                    + " — searching full structure bounds");
            BlockPos min = data.getStructureOrigin();
            BlockPos max = data.getStructureEnd();
            List<Painting> all = level.getEntitiesOfClass(
                    Painting.class,
                    new AABB(min, max).inflate(4.0)
            );
            if (!all.isEmpty()) {
                BlockPos bed = data.getBedPos();
                painting = all.stream()
                        .min(Comparator.comparingDouble(p ->
                                p.distanceToSqr(bed.getX(), bed.getY(), bed.getZ())))
                        .orElse(null);

                // update stored pos so future calls find it correctly
                if (painting != null) {
                    BlockPos corrected = painting.blockPosition()
                            .relative(painting.getDirection());
                    data.setPaintingPos(corrected);
                    System.out.println("Painting pos corrected to: " + corrected);
                }
            }
        }

        if (painting == null) {
            System.err.println("No painting found anywhere in structure — "
                    + "use /setstructure painting to set manually");
            return;
        }

        BlockPos pos = painting.blockPosition();
        Direction facing = painting.getDirection();
        painting.discard();

        var registry = level.registryAccess()
                .registryOrThrow(Registry.PAINTING_VARIANT_REGISTRY);
        var variantHolder = registry.getHolder(
                ResourceKey.create(Registry.PAINTING_VARIANT_REGISTRY, variantId));

        if (variantHolder.isEmpty()) {
            System.err.println("Painting variant not found: " + variantId);
            return;
        }

        Painting newPainting = new Painting(level, pos, facing, variantHolder.get());
        level.addFreshEntity(newPainting);
        System.out.println("Painting updated to: " + variantId + " at " + pos);
    }

    private static Painting findNearest(ServerLevel level, BlockPos pos, double radius) {
        List<Painting> paintings = level.getEntitiesOfClass(
                Painting.class,
                new AABB(pos).inflate(radius)
        );
        if (paintings.isEmpty()) return null;
        return paintings.stream()
                .min(Comparator.comparingDouble(p ->
                        p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())))
                .orElse(null);
    }
}