package bunger.group;

import bunger.group.alex.CreativeTab;
import bunger.group.alex.spell.LearnSpellPacket;
import bunger.group.alex.Mana;
import bunger.group.alex.ManaPacket;
import bunger.group.alex.ParticleHelpers;
import bunger.group.alex.SpellHelpers;
import bunger.group.alex.block.ModBlocks;
import bunger.group.alex.block.entity.ModBlockEntities;
import bunger.group.alex.item.ModItems;
import bunger.group.alex.menu.ModMenuType;
import bunger.group.alex.menu.SpellDeskMenu;
import bunger.group.csmit863.CustomSounds;
import bunger.group.csmit863.Madness;
import bunger.group.csmit863.biome.ModBiomes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		//

		// Packets
		PayloadTypeRegistry.clientboundPlay().register(ManaPacket.TYPE, ManaPacket.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(LearnSpellPacket.TYPE, LearnSpellPacket.CODEC);

		// Items
		ModItems.register();
		bunger.group.csmit863.item.ModItems.register();

		// Blocks
		ModBlocks.register();
		ModBlockEntities.initialize();
		bunger.group.csmit863.block.ModBlocks.initialize();

		// Entities
		bunger.group.csmit863.entity.ModEntityTypes.registerModEntityTypes();
		bunger.group.csmit863.entity.ModEntityTypes.registerAttributes();

		// Biomes
		ModBiomes.initialise(); //csmit mod biomes

		// Sounds
		CustomSounds.initialize(); //csmit custom sounds

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
					Madness.MadnessData madness = Madness.get(player);
					mana.recalculateMaxMana();
					if (mana.getCurrentMana() < mana.getMaxMana()) {
						mana.incrementCurrentMana();
						ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
					}

					if ((!player.hasEffect(bunger.group.csmit863.item.ModItems.HALLUCINATION_EFFECT)) && (madness.getCurrentMadness() != 0) ) {
						if (server.overworld().getRandom().nextInt(5) == 0) {  // ~20% chance per tick
							madness.decrementCurrentMadness();
						}
					}

					// Teleport to mad realm at 100 madness
					if (madness.getCurrentMadness() >= 100) {
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
							madness.setCurrentMadness(0);
						}
					}
				}
			}
		});

		// Creative tab
		CreativeTab.register();

		// Mana init on join
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			Mana.ManaData mana = Mana.get(player);
			Madness.MadnessData madness = Madness.get(player);
			if (mana.getMaxMana() == 0) {
				mana.setMaxMana(100);
				mana.setCurrentMana(100);
				madness.setMaxMadness(100);
			}
			ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
		});
	}
}