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
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutuallyAssuredDestruction implements ModInitializer {
	public static final String MOD_ID = "mutually-assured-destruction";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Packets
		PayloadTypeRegistry.clientboundPlay().register(ManaPacket.TYPE, ManaPacket.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(LearnSpellPacket.TYPE, LearnSpellPacket.CODEC);

		// Items
		ModItems.register();

		// Blocks
		ModBlocks.register();
		ModBlockEntities.initialize();

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
				}
			}
		});

		// Creative tab
		CreativeTab.register();

		// Mana init on join
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			Mana.ManaData mana = Mana.get(player);
			if (mana.getMaxMana() == 0) {
				mana.setMaxMana(100);
				mana.setCurrentMana(100);
			}
			ServerPlayNetworking.send(player, new ManaPacket(mana.getCurrentMana(), mana.getMaxMana()));
		});
	}
}