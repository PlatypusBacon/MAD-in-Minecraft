package bunger.group.event;

import bunger.group.data.StructureEventData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class PaintingUpdater {

    private static final double SEARCH_RADIUS = 3.0;

    private static final ResourceLocation[] VARIANTS = {
            new ResourceLocation("mutually-assured-destruction", "god_coming_0"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_1"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_2"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_3"),
            new ResourceLocation("mutually-assured-destruction", "god_coming_4"),
    };

    // called from SundownWatcher with a specific index
    public static void updatePaintingToIndex(ServerLevel level,
                                             StructureEventData data,
                                             int index) {
        BlockPos paintingPos = data.getPaintingPos();
        int clampedIndex = Math.max(0, Math.min(index, VARIANTS.length - 1));
        applyVariant(level, paintingPos, VARIANTS[clampedIndex]);
    }

    // called from the old flow with auto-advancing index
    public static void updatePainting(ServerLevel level, StructureEventData data) {
        int nextIndex = (data.getPaintingIndex() + 1) % VARIANTS.length;
        updatePaintingToIndex(level, data, nextIndex);
    }

    private static void applyVariant(ServerLevel level,
                                     BlockPos searchPos,
                                     ResourceLocation variantId) {
        List<Painting> paintings = level.getEntitiesOfClass(
                Painting.class,
                new AABB(searchPos).inflate(SEARCH_RADIUS)
        );

        if (paintings.isEmpty()) {
            System.err.println("No painting found near " + searchPos);
            return;
        }

        Painting painting = paintings.get(0);
        BlockPos pos      = painting.blockPosition();
        Direction facing  = painting.getDirection();
        painting.discard();

        // look up variant from registry
        var registry = level.registryAccess()
                .registryOrThrow(Registry.PAINTING_VARIANT_REGISTRY);

        var variantHolder = registry.getHolder(
                ResourceKey.create(Registry.PAINTING_VARIANT_REGISTRY, variantId));

        if (variantHolder.isEmpty()) {
            System.err.println("Painting variant not found: " + variantId);
            return;
        }

        // use the correct constructor that takes a Holder
        Painting newPainting = new Painting(level, pos, facing, variantHolder.get());
        level.addFreshEntity(newPainting);

        System.out.println("Painting updated to: " + variantId);
    }
}