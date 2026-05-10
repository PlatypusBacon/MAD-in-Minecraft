package bunger.group;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bunger.group.alex.Bunger1;

import bunger.group.bryan.Bunger2;
import bunger.group.bryan.TaxItem;
import bunger.group.bryan.TaxLogic;
import bunger.group.bryan.MailboxBlock;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import com.mojang.serialization.Codec;
// import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Registry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import java.util.*;
import net.minecraft.world.level.Level; 


import bunger.group.csmit863.Bunger3;
import bunger.group.ethan.Bunger4;
import bunger.group.tyler.Bunger5;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	// ================ START OF BRYAN'S INITIALIZATION SECTION ================

	public static final Block MAILBOX_BLOCK = MailboxBlock.registerBlock("mailbox_block",
		MailboxBlock::new,
		BlockBehaviour.Properties.of().strength(15.0F).requiresCorrectToolForDrops(),
		true
    );

	public static final DataComponentType<String> TAX_ITEMS =
		Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(MutuallyAssuredDestruction.MOD_ID, "tax_items"),
			DataComponentType.<String>builder()
				.persistent(Codec.STRING)
				.build()
    	);

	public static final DataComponentType<Long> TAX_DUE_DAY =
    	Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(MOD_ID, "tax_due_day"),
			DataComponentType.<Long>builder()
				.persistent(Codec.LONG)
				.build()
    );

	public static final DataComponentType<Boolean> TAX_PAID =
		Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			Identifier.fromNamespaceAndPath(MOD_ID, "tax_paid"),
			DataComponentType.<Boolean>builder()
				.persistent(Codec.BOOL)
				.build()
    );






	// ================ END OF BRYAN'S INITIALIZATION SECTION ================


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Bunger2.initialize();

		TaxItem.initialize();
		ServerTickEvents.END_SERVER_TICK.register(server -> {

			long day = server.overworld().getGameTime() % 24000L;

			// run once per day
			if (day == 0) {

				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					TaxLogic.checkTaxes(player);
				}
			}
		});

      	CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> creativeTab.accept(MAILBOX_BLOCK));
		//CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(TaxItem.TAX_ITEM));


		LOGGER.info("Hello Fabric world!");
	}
}