package bunger.group;

import bunger.group.tyler.block.ModBlocks;
import bunger.group.tyler.command.SetupStructureCommand;
import bunger.group.tyler.entity.ModEntities;
import bunger.group.tyler.event.SundownWatcher;
import bunger.group.tyler2.event.Trip;
import bunger.group.tyler.event.god.StructureManager;
import bunger.group.tyler.item.ModItems;
import bunger.group.tyler.sound.ModSounds;
import bunger.group.tyler.structure.ModStructures;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModStructures.register();
		Trip.register();
		ModBlocks.registerModBlocks();
		ModSounds.initialize();
		TickScheduler.register();
		StructureManager.register();
		SundownWatcher.register();
		SetupStructureCommand.register();
		ModItems.registerModItems();
		ModEntities.registerModEntityTypes();
		ModEntities.registerAttributes();
	}
}