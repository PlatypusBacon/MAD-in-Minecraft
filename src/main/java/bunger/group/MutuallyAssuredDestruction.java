package bunger.group;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import bunger.group.ethan.ProphetEntity;
import bunger.group.ethan.ModEntityTypes;
import bunger.group.ethan.RedDarknessEffect;
// import net.minecraft.resources.ResourceLocation;
import bunger.group.ethan.RedRainHandler;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Map<UUID, Long> RED_RAIN_PLAYERS = new HashMap<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");



		// ----------Ethans registrations----------
		ProphetEntity.initialize();
		ModEntityTypes.registerModEntityTypes();
		ModEntityTypes.registerAttributes();
		Registry.register(BuiltInRegistries.SOUND_EVENT, 
			Identifier.fromNamespaceAndPath("mutually-assured-destruction", "heartbeat"), 
			ProphetEntity.HEARTBEAT);
		Registry.register(BuiltInRegistries.SOUND_EVENT, 
			Identifier.fromNamespaceAndPath("mutually-assured-destruction", "dripping"), 
			ProphetEntity.DRIPPING);
		Registry.register(BuiltInRegistries.MOB_EFFECT,
			Identifier.fromNamespaceAndPath("mutually-assured-destruction", "red_darkness"),
			RedDarknessEffect.RED_DARKNESS);
		RedRainHandler.register();


		// ------------------------------------------
	}
}