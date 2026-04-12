package bunger.group;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bunger.group.alex.Bunger1;
import bunger.group.bryan.Bunger2;
import bunger.group.csmit863.Bunger3;
import bunger.group.ethan.Bunger4;
import bunger.group.tyler.Bunger5;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import bunger.group.ethan.ProphetEntity;
import bunger.group.ethan.ModEntityTypes;
// import net.minecraft.resources.ResourceLocation;

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

		LOGGER.info("Hello Fabric world!");
		// Registry.register(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "custom_item"), Bunger4.CUSTOM_ITEM);
		Bunger4.initialize();
		ProphetEntity.initialize();
		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerAttributes();
		Registry.register(BuiltInRegistries.SOUND_EVENT, 
			Identifier.fromNamespaceAndPath("mutually-assured-destruction", "heartbeat"), 
			ProphetEntity.HEARTBEAT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, 
			Identifier.fromNamespaceAndPath("mutually-assured-destruction", "dripping"), 
			ProphetEntity.DRIPPING);
	}
}