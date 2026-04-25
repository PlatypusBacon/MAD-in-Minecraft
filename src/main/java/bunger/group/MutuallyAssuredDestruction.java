package bunger.group;

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

		bunger.group.tyler.structure.ModStructures.register();
		bunger.group.tyler2.event.Trip.register();
		bunger.group.tyler.block.ModBlocks.registerModBlocks();
		bunger.group.tyler.sound.ModSounds.initialize();
		bunger.group.tyler.event.TickScheduler.register();
		bunger.group.tyler.event.god.StructureManager.register();
		bunger.group.tyler.event.SundownWatcher.register();
		bunger.group.tyler.command.SetupStructureCommand.register();
		bunger.group.tyler.item.ModItems.registerModItems();
		bunger.group.tyler2.item.ModItems.registerModItems();
		bunger.group.tyler2.block.ModBlocks.registerModBlocks();
		bunger.group.tyler.entity.ModEntities.registerModEntityTypes();
		bunger.group.tyler.entity.ModEntities.registerAttributes();
		bunger.group.tyler2.item.ModCreativeTabs.registerCreativeTabs();
		bunger.group.tyler.item.ModCreativeTabs.registerCreativeTabs();
		bunger.group.tyler.ModCombatEvents.register();
		bunger.group.tyler.net.PunchSidePacket.registerServer();
	}
}