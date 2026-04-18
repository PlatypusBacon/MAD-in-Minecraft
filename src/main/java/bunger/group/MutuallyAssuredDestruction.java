package bunger.group;

import bunger.group.alex.CreativeTab;
import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import bunger.group.alex.block.ModBlocks;
import bunger.group.alex.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
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

		//-----Alex-----
		// Items
		ModItems.register();

		// Blocks
		ModBlocks.register();

		// Tick Events
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			SpellHelpers.tick();
			ParticleHelpers.tick();
		});

		//Creative Tab
		CreativeTab.register();


	}
}