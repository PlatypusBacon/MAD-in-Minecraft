package bunger.group;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Alex
import bunger.group.alex.wizardry.items.spells.SpellRegistry;
import bunger.group.alex.wizardry.ParticleHelpers;
import bunger.group.alex.wizardry.SpellHelpers;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SpellRegistry.register();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerTickEvents.END_SERVER_TICK.register(server -> {
            ParticleHelpers.tick();
			SpellHelpers.tick();
        });

		LOGGER.info("Hello Fabric world!");
	}
}