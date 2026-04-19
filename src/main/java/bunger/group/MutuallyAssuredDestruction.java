package bunger.group;

import bunger.group.alex.CreativeTab;
import bunger.group.alex.Mana;
import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import bunger.group.alex.block.ModBlocks;
import bunger.group.alex.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
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

		bunger.group.csmit863.item.ModItems.register();

		bunger.group.csmit863.entity.ModEntityTypes.registerModEntityTypes();
		bunger.group.csmit863.entity.ModEntityTypes.registerAttributes();

		// Tick Events
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			SpellHelpers.tick();
			ParticleHelpers.tick();

			// regens everyone 10 mana a second, i can play around with later
			if (server.getTickCount() % 2 == 0) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					Mana.ManaData mana = Mana.get(player);
					if (mana.getCurrentMana() < mana.getMaxMana()) {
						mana.incrementCurrentMana();
					}
				}
			}
		});

		//Creative Tab
		CreativeTab.register();

		// Mana init, set new players to 100 mana
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayer player) {
				Mana.ManaData mana = Mana.get(player);
				if (mana.getMaxMana() == 0) {
					mana.setMaxMana(100);
					mana.setCurrentMana(100);
				}
			}
		});

	}
}