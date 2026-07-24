package bunger.group;

import bunger.group.csmit863.CustomSounds;
import bunger.group.csmit863.Madness;
import bunger.group.csmit863.biome.ModBiomes;
import bunger.group.tyler.item.ModItems;
import bunger.group.tyler2.block.ModBlockEntities;
import bunger.group.tyler2.block.ModBlocks;
import bunger.group.tyler3.RegisterSpawns;

import bunger.group.tyler3.StripperDetector;
import bunger.group.tyler3.network.UnlockRecipePagePayload;
import bunger.group.tyler3.rego.RecipePageRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bunger.group.alex.Bunger1;

import bunger.group.bryan.Bunger2;
import bunger.group.bryan.ChestTracker;
import bunger.group.bryan.TaxItem;
import bunger.group.bryan.TaxLogic;
import bunger.group.bryan.MailboxBlock;
import bunger.group.bryan.StorageEntityTracker;
import bunger.group.bryan.NoticeItem;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.entity.vehicle.boat.ChestRaft;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;



import bunger.group.csmit863.Bunger3;
import bunger.group.ethan.AltarFragmentBlock;
import bunger.group.ethan.AltarBlock;
import bunger.group.ethan.AltarEventHandler;
import bunger.group.tyler.Bunger5;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.InteractionHand;
import bunger.group.ethan.ProphetEntity;
import bunger.group.ethan.VoremothEntity;
import bunger.group.ethan.ModEntityTypes;
//import bunger.group.ethan.ModItems;
import bunger.group.ethan.RedDarknessEffect;
// import net.minecraft.resources.ResourceLocation;
import bunger.group.ethan.RedRainHandler;
import bunger.group.ethan.VoremothArmorMaterial;
import bunger.group.ethan.VoremothBossMechanic;
import bunger.group.ethan.VoremothCrownHandler;
import bunger.group.ethan.VoremothCrownPacket;

// Alex Imports
import bunger.group.alex.effect.ModEffects;
import bunger.group.alex.entity.EntityLootUpdater;
import bunger.group.alex.item.potion.ModPotions;
import bunger.group.alex.spell.LearnSpellPacket;
import bunger.group.alex.Mana;
import bunger.group.alex.ManaPacket;
import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.spell.SpellHelpers;

import bunger.group.alex.menu.ModMenuType;
import bunger.group.alex.menu.SpellDeskMenu;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import bunger.group.csmit863.MadnessPacket;
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


	// ================ START OF ETHAN'S INITIALIZATION SECTION ================
	public static final Map<UUID, Long> RED_RAIN_PLAYERS = new HashMap<>();

	public static final Block ALTAR_FRAGMENT = AltarFragmentBlock.registerBlock("altar_fragment",
        AltarFragmentBlock::new,
        BlockBehaviour.Properties.of().strength(4.0F),
        true
    );

	public static final Block ALTAR = AltarBlock.registerBlock("altar",
        AltarBlock::new,
        BlockBehaviour.Properties.of().strength(4.0F),
        true
    );

	public static final Item VOREMOTH_CROWN = bunger.group.ethan.ModItems.register(
        "voremoth_crown",
        Item::new,
        new Item.Properties().humanoidArmor(VoremothArmorMaterial.INSTANCE, ArmorType.HELMET)
                .durability(ArmorType.HELMET.getDurability(VoremothArmorMaterial.BASE_DURABILITY))
    );


	public static final SoundEvent ALTAR_FORM = SoundEvent.createVariableRangeEvent(
    	Identifier.fromNamespaceAndPath(MOD_ID, "altar_form")
	);

	public static final SoundEvent VOREMOTH_HIT = SoundEvent.createFixedRangeEvent(
    	Identifier.fromNamespaceAndPath(MOD_ID, "voremoth_hit"), 100.0F);
	public static final SoundEvent VOREMOTH_CHARGE = SoundEvent.createFixedRangeEvent(
    	Identifier.fromNamespaceAndPath(MOD_ID, "voremoth_charge"), 100.0F);
	public static final SoundEvent VOREMOTH1 = SoundEvent.createFixedRangeEvent(
    	Identifier.fromNamespaceAndPath(MOD_ID, "voremoth1"), 100.0F);
	public static final SoundEvent VOREMOTH2 = SoundEvent.createFixedRangeEvent(
		Identifier.fromNamespaceAndPath(MOD_ID, "voremoth2"), 100.0F);
	public static final SoundEvent VOREMOTH3 = SoundEvent.createFixedRangeEvent(
		Identifier.fromNamespaceAndPath(MOD_ID, "voremoth3"), 100.0F);
	public static final SoundEvent VOREMOTH4 = SoundEvent.createFixedRangeEvent(
		Identifier.fromNamespaceAndPath(MOD_ID, "voremoth4"), 100.0F);
	public static final SoundEvent VOREMOTH5 = SoundEvent.createFixedRangeEvent(
		Identifier.fromNamespaceAndPath(MOD_ID, "voremoth5"), 100.0F);
	public static final SoundEvent VOREMOTH_AMBIENT = SoundEvent.createVariableRangeEvent(
		Identifier.fromNamespaceAndPath(MOD_ID, "voremoth_ambient"));

	// ================ END OF ETHAN'S INITIALIZATION SECTION ================



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//Bunger2.initialize();

		TaxItem.initialize();
		NoticeItem.initialize();


				// ================ CHECK TAXES ================
		ServerTickEvents.END_SERVER_TICK.register(server -> {

			long day = server.overworld().getGameTime() % 24000L;

			// run once per day
			if (day == 0) {

				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					TaxLogic.checkTaxes(player, server);
				}
			}
		});



		// ============ GIVE PLAYERS TAXES =============
		ServerTickEvents.END_SERVER_TICK.register(server -> {

			long day = server.overworld().getGameTime() % (101L);

			// stagger check and give by 100 ticks so no overlap
			if (day == 100) {

				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					
					// Create tax slip
					ItemStack taxSlip = new ItemStack(TaxItem.TAX_ITEM);
					TaxLogic.applyTaxBook(taxSlip, player);

					// Get current offhand item
					ItemStack offhand = player.getOffhandItem();

					if (!offhand.isEmpty()) {
						// Drop it on the ground
						player.drop(offhand.copy(), true);

						// Clear the offhand
						player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
					}

					// Put the tax slip in the offhand
					player.setItemInHand(InteractionHand.OFF_HAND, taxSlip);

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


		// ================ CHECK ENTITIES ================
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

		LOGGER.info("Hello Fabric world!");
		bunger.group.tyler.structure.ModStructures.register();
		bunger.group.tyler2.event.Trip.register();
		bunger.group.tyler.block.ModBlocks.registerModBlocks();
		bunger.group.tyler.sound.ModSounds.initialize();
		bunger.group.tyler3.sounds.ModSounds.initialize();
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				ResourceKey.create(
						Registries.PLACED_FEATURE,
						Identifier.fromNamespaceAndPath(MOD_ID, "rock")
				)
		);
        StripperDetector.register();
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
		ModBlockEntities.registerModBlockEntities();
		bunger.group.tyler2.item.ModCreativeTabs.registerCreativeTabs();
		bunger.group.tyler.item.ModCreativeTabs.registerCreativeTabs();
		bunger.group.tyler.ModCombatEvents.register();
		bunger.group.tyler.net.PunchSidePacket.registerServer();
		bunger.group.tyler3.network.ModNet.register();
		RegisterSpawns.register();
		PayloadTypeRegistry.clientboundPlay().register(
				UnlockRecipePagePayload.TYPE,
				UnlockRecipePagePayload.CODEC
		);
		RecipePageRegistry.register(ModItems.SQUIRREL_STAPELER, "bear_boxers");
		RecipePageRegistry.register(ItemTags.PLANKS, "crafting_table");
		RecipePageRegistry.register(ModBlocks.ROCK.asItem(), "hot_plate");
		RecipePageRegistry.register(bunger.group.tyler2.item.ModItems.ANTLER, "antler_pickaxe");
		RecipePageRegistry.register(bunger.group.tyler2.item.ModItems.HOT_PLATE, "copper_ingot");
		RecipePageRegistry.register(Items.STICK, "tanning_rack");
		RecipePageRegistry.register(bunger.group.tyler3.item.ModItems.MEDIUM_AMMO, "bullet");
		RecipePageRegistry.register(Items.COPPER_INGOT, "mailbox");



		// ----------Ethans registrations----------

		ProphetEntity.initialize();

		SpawnPlacements.register(
			ModEntityTypes.PROPHET,
			SpawnPlacementTypes.ON_GROUND,
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
			(entityType, level, spawnReason, pos, random) -> {
				return level.getMaxLocalRawBrightness(pos) <= 7;
			}
		);

		BiomeModifications.
			addSpawn(
				BiomeSelectors.foundInOverworld(),
				MobCategory.MONSTER,
				ModEntityTypes.PROPHET,
				10,
				1,
				1
			);

		VoremothEntity.initialize();

		VoremothBossMechanic.register();
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
		AltarEventHandler.register();
		VoremothCrownHandler.register();
		VoremothCrownPacket.registerPacket();
		Registry.register(BuiltInRegistries.SOUND_EVENT,
    		Identifier.fromNamespaceAndPath(MOD_ID, "altar_form"),
    		ALTAR_FORM);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth1"),
			VOREMOTH1);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth2"),
			VOREMOTH2);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth3"),
			VOREMOTH3);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth4"),
			VOREMOTH4);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth5"),
			VOREMOTH5);
		Registry.register(BuiltInRegistries.SOUND_EVENT,
			Identifier.fromNamespaceAndPath(MOD_ID, "voremoth_ambient"),
			VOREMOTH_AMBIENT);




		// ------------------------------------------
		// csmit init stuff
		bunger.group.csmit863.block.ModBlocks.initialize();
		bunger.group.csmit863.item.ModItems.register();
		bunger.group.csmit863.entity.ModEntityTypes.registerModEntityTypes();
		bunger.group.csmit863.entity.ModEntityTypes.registerAttributes();
		CustomSounds.initialize();
		ModBiomes.initialise();
		PayloadTypeRegistry.clientboundPlay().register(MadnessPacket.TYPE, MadnessPacket.CODEC);
		// ------------------------------------------


		// ------------------------------------------
		// Alex Innit stuff
		// Packets
		PayloadTypeRegistry.clientboundPlay().register(ManaPacket.TYPE, ManaPacket.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(LearnSpellPacket.TYPE, LearnSpellPacket.CODEC);

		// Effects
        bunger.group.alex.effect.ModEffects.register();

		// Items
        bunger.group.alex.item.ModItems.register();
		bunger.group.alex.item.potion.ModPotions.register();

		// Blocks
		bunger.group.alex.block.ModBlocks.register();
		bunger.group.alex.block.entity.ModBlockEntities.initialize();

		// Mobs
		bunger.group.alex.entity.ModEntityTypes.registerModEntityTypes();
		bunger.group.alex.entity.ModEntityTypes.registerAttributes();
		EntityLootUpdater.update_loot_pools();

		// Menus
		ModMenuType.initialize();

		// Handle spell learning on server
		ServerPlayNetworking.registerGlobalReceiver(LearnSpellPacket.TYPE, (payload, context) -> {
			context.server().execute(() -> {
				if (context.player().containerMenu instanceof SpellDeskMenu menu) {
					menu.tryLearnSpell(context.player(), payload.spellIndex());
				}
			});
		});

		// Tick events
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			SpellHelpers.tick();
			ParticleHelpers.tick();

			if (server.getTickCount() % 2 == 0) {
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					Mana.ManaData mana = Mana.get(player);
					mana.recalculateMaxMana();

					if (mana.getCurrentMana() < mana.getMaxMana()) {
						mana.incrementCurrentMana();
						ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
					}

					Madness.MadnessData madness = Madness.get(player);
					if ((!player.hasEffect(bunger.group.csmit863.item.ModItems.HALLUCINATION_EFFECT)) && (madness.getCurrentMadness() != 0) ) {
						if (server.overworld().getRandom().nextInt(5) == 0) {
							madness.decrementCurrentMadness();
						}
					}
					ServerPlayNetworking.send(player, new MadnessPacket(madness.getCurrentMadness(), madness.getMaxMadness())); // NEW

					// Teleport to mad realm at 100 madness
					if (madness.getCurrentMadness() >= madness.getMaxMadness()) {
						ServerLevel madRealm = server.getLevel(ModBiomes.MAD_REALM);
						if (madRealm != null && !player.level().dimension().equals(ModBiomes.MAD_REALM)) {
							player.teleportTo(
									madRealm,
									0.0, 65.0, 0.0,
									java.util.Set.of(),
									player.getYRot(),
									player.getXRot(),
									true
							);
						}
					}

					// Send back to spawn at 0 madness
					// Send back to spawn at 0 madness
					if (madness.getCurrentMadness() <= 0 && player.level().dimension().equals(ModBiomes.MAD_REALM)) {
						ServerLevel overworld = server.overworld();
						BlockPos spawnPos;
						if (player.getRespawnConfig() != null) {
							spawnPos = player.getRespawnConfig().respawnData().pos();
						} else {
							spawnPos = overworld.getRespawnData().pos();
						}
						player.teleportTo(
								overworld,
								spawnPos.getX(),
								spawnPos.getY(),
								spawnPos.getZ(),
								java.util.Set.of(),
								player.getYRot(),
								player.getXRot(),
								true
						);
					}
				}
			}
		});

		// Creative tab
		bunger.group.alex.CreativeTab.register();

		// Mana init and slot init on join
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			Mana.ManaData mana = Mana.get(player);
			Madness.MadnessData madness = Madness.get(player);
			if (mana.getMaxMana() == 0) {
				mana.setMaxMana(50);
				mana.setCurrentMana(50);
				madness.setMaxMadness(200);
			}
			ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
			ServerPlayNetworking.send(player, new MadnessPacket(madness.getCurrentMadness(), madness.getMaxMadness())); // NEW
		});



		// END Alex Innit stuff
	}
}