package bunger.group.tyler2.block;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.tyler2.entity.HotPlateBlockEntity;
import bunger.group.tyler2.entity.TanningRackBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final BlockEntityType<HotPlateBlockEntity> HOT_PLATE_BE =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "hot_plate"),
                    FabricBlockEntityTypeBuilder.<HotPlateBlockEntity>create(
                            HotPlateBlockEntity::new,
                            ModBlocks.HOT_PLATE
                    ).build()
            );

    public static final BlockEntityType<TanningRackBlockEntity> TANNING_RACK_BE =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "tanning_rack"),
                    FabricBlockEntityTypeBuilder.<TanningRackBlockEntity>create(
                            TanningRackBlockEntity::new,
                            ModBlocks.TANNING_RACK
                    ).build()
            );

    public static void registerModBlockEntities() {
        HotPlateBlockEntity.TYPE    = HOT_PLATE_BE;
        TanningRackBlockEntity.TYPE = TANNING_RACK_BE;
        MutuallyAssuredDestruction.LOGGER.debug(
                "Registering block entities for " + MutuallyAssuredDestruction.MOD_ID);
    }
}