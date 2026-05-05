package bunger.group;

import bunger.group.tyler3.RegisterSpawns;
import bunger.group.tyler3.entity.DudeEntity;
import bunger.group.tyler3.entity.ModEntities;
import bunger.group.tyler3.item.GoldScarItem;
import bunger.group.tyler3.network.ReloadPacket;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
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
		bunger.group.tyler3.sounds.ModSounds.initialize();

		bunger.group.tyler.event.TickScheduler.register();
		bunger.group.tyler.event.god.StructureManager.register();
		bunger.group.tyler.event.SundownWatcher.register();
		bunger.group.tyler.command.SetupStructureCommand.register();
		bunger.group.tyler.item.ModItems.registerModItems();
		bunger.group.tyler2.item.ModItems.registerModItems();
		bunger.group.tyler3.item.ModItems.registerModItems();
		bunger.group.tyler2.block.ModBlocks.registerModBlocks();
		bunger.group.tyler.entity.ModEntities.registerModEntityTypes();
		bunger.group.tyler.entity.ModEntities.registerAttributes();
		bunger.group.tyler3.entity.ModEntities.registerModEntityTypes();
		bunger.group.tyler3.entity.ModEntities.registerAttributes();
		bunger.group.tyler2.item.ModCreativeTabs.registerCreativeTabs();
		bunger.group.tyler.item.ModCreativeTabs.registerCreativeTabs();

		bunger.group.tyler.ModCombatEvents.register();
		bunger.group.tyler.net.PunchSidePacket.registerServer();
		bunger.group.tyler3.network.ModNet.register();
		RegisterSpawns.register();
	}
}