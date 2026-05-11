package bunger.group;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bunger.group.alex.Bunger1;

import bunger.group.bryan.Bunger2;
import bunger.group.bryan.ChestTracker;
import bunger.group.bryan.TaxItem;
import bunger.group.bryan.TaxLogic;
import bunger.group.bryan.MailboxBlock;
import bunger.group.bryan.StorageEntityTracker;
import java.util.List;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.boat.ChestRaft;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import com.mojang.serialization.Codec;
// import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import java.util.*;
import net.minecraft.world.level.Level; 
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;


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
		BlockBehaviour.Properties.of().strength(5.0F).requiresCorrectToolForDrops(),
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

	public static final TagKey<Item> FOOD_TAXES = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(
			MutuallyAssuredDestruction.MOD_ID,
			"food_taxes"
		)
	);

	public static final TagKey<Item> STONE_TAXES = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(
			MutuallyAssuredDestruction.MOD_ID,
			"stone_taxes"
		)
	);

	public static final TagKey<Item> WOOD_TAXES = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(
			MutuallyAssuredDestruction.MOD_ID,
			"wood_taxes"
		)
	);

	public static final TagKey<Item> ORE_TAXES = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(
			MutuallyAssuredDestruction.MOD_ID,
			"ore_taxes"
		)
	);

	public static final TagKey<Block> TAXABLE_STORAGE = TagKey.create(
		Registries.BLOCK,
		Identifier.fromNamespaceAndPath(
			MutuallyAssuredDestruction.MOD_ID,
			"taxable_storage"
		)
	);



	// ================ END OF BRYAN'S INITIALIZATION SECTION ================


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Bunger2.initialize();

		TaxItem.initialize();

		// ============ GIVE PLAYERS TAXES =============
		ServerTickEvents.END_SERVER_TICK.register(server -> {

			long day = server.overworld().getGameTime() % 100L;

			// run once per day
			if (day == 0) {

				for (ServerPlayer player : server.getPlayerList().getPlayers()) {

					// create tax slip
					ItemStack taxSlip =
						new ItemStack(TaxItem.TAX_ITEM);

					// generate taxes/book text
					TaxLogic.applyTaxBook(taxSlip, player);

					// give to player
					player.getInventory().add(taxSlip);
				}
			}
		});

		// ================ CHECK TAXES ================
		ServerTickEvents.END_SERVER_TICK.register(server -> {

			long day = server.overworld().getGameTime() % 100L;

			// run once per day
			if (day == 0) {

				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					TaxLogic.checkTaxes(player, server);
				}
			}
		});

		// ================ CHEST POS ================
		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {

            if (world.isClientSide()){
				return InteractionResult.PASS;
			}
			
            BlockPos pos = hit.getBlockPos();
            BlockState state = world.getBlockState(pos);

			if (state.is(TAXABLE_STORAGE)){
				ChestTracker.add(world.dimension(), pos);
			}

            return InteractionResult.PASS;
        });



		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {

			if (!world.isClientSide()) {

				if (
					entity instanceof ChestBoat ||
					entity instanceof MinecartChest ||
					entity instanceof ChestRaft
				) {

					StorageEntityTracker.STORAGE_ENTITIES.add(entity.getUUID());
				}
			}

			return InteractionResult.PASS;
		});


      	CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register((creativeTab) -> creativeTab.accept(MAILBOX_BLOCK));
		//CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register((creativeTab) -> creativeTab.accept(TaxItem.TAX_ITEM));


		LOGGER.info("Hello Fabric world!");
	}
}