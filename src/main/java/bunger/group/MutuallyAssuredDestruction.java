package bunger.group;

import bunger.group.command.SetupStructureCommand;
import bunger.group.entity.GodEntity;
import bunger.group.entity.ModEntities;
import bunger.group.entity.SquirrelEntity;
import bunger.group.event.StructureManager;
import bunger.group.event.SundownWatcher;
import bunger.group.item.ModItems;
import bunger.group.sound.ModSounds;
import bunger.group.structure.ModStructures;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bunger.group.block.ModBlocks;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final PaintingVariant GOD_COMING_0 = registerPainting("god_coming_0", 64, 48);
	public static final PaintingVariant GOD_COMING_1 = registerPainting("god_coming_1", 64, 48);
	public static final PaintingVariant GOD_COMING_2 = registerPainting("god_coming_2", 64, 48);
	public static final PaintingVariant GOD_COMING_3 = registerPainting("god_coming_3", 64, 48);
	public static final PaintingVariant GOD_COMING_4 = registerPainting("god_coming_4", 64, 48);

	private static PaintingVariant registerPainting(String name, int width, int height) {
		return Registry.register(
				Registry.PAINTING_VARIANT,
				new ResourceLocation(MOD_ID, name),
				new PaintingVariant(width, height)
		);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModStructures.register();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.register();
		FabricDefaultAttributeRegistry.register(
				ModEntities.SQUIRREL,
				SquirrelEntity.createAttributes()
		);
		FabricDefaultAttributeRegistry.register(
				ModEntities.GOD, GodEntity.createAttributes());
		StructureManager.register();
		SundownWatcher.register();
		SetupStructureCommand.register();

	}
}