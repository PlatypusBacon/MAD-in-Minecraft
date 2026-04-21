package bunger.group.ethan;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import java.util.HashMap;
import java.util.Map;

import bunger.group.MutuallyAssuredDestruction;
import bunger.group.ethan.RedDarknessEffect;
import bunger.group.ethan.VoremothEntity;

// TODO: handle player death

public class VoremothBossMechanic {
    
    public enum BeamColor { RED, BLUE, WHITE }
    
    private static final int CYCLE_DURATION = 300; // 15 seconds
    
    // stores active beam positions and colors
    public static final Map<BlockPos, BeamColor> ACTIVE_BEAMS = new HashMap<>();
    public static BeamColor REQUIRED_COLOR = BeamColor.RED;
    private static int cycleTimer = 0;
    private static VoremothEntity activeVoremoth = null;
    private static int SURFACE_Y_LEVEL = 63;
    private static int damageTimer = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
        if (activeVoremoth == null || activeVoremoth.isRemoved()) {
            for (ServerLevel level : server.getAllLevels()) {
                for (Entity entity : level.getAllEntities()) {
                    if (entity instanceof VoremothEntity voremoth && !voremoth.isRemoved()) {
                        System.out.println("VOREMOTHTEST: Re-found voremoth after removal");
                        activeVoremoth = voremoth;
                        break;
                    }
                }
            }
            if (activeVoremoth == null || activeVoremoth.isRemoved()) {
                ACTIVE_BEAMS.clear();
                return;
            }
        }

            cycleTimer++;
            damageTimer++;

            // spawn fake beam particles every tick
            for (ServerLevel level : server.getAllLevels()) {
                for (Map.Entry<BlockPos, BeamColor> entry : ACTIVE_BEAMS.entrySet()) {
                    spawnBeamParticles(level, entry.getKey(), entry.getValue());
                }

                // check players standing in beams
                for (ServerPlayer player : level.players()) {
                    checkPlayerInBeam(player, level);

                }
       
            // System.out.println("Boss invulnerable to beacon damage: " + activeVoremoth.isInvulnerableTo(level, level.damageSources().source(ModDamageTypes.BEACON_DAMAGE)) + " | cycleTimer: " + cycleTimer + " | level: " + level);
            // System.out.println("Is removed: " + activeVoremoth.isRemoved());


            }

            // reset cycle every 15 seconds
            if (cycleTimer >= CYCLE_DURATION) {
                startNewCycle(server);
            }

        });
    }

    public static void startMechanic(VoremothEntity voremoth, BlockPos altarPos) {
        SURFACE_Y_LEVEL = altarPos.getY() - 10;
        System.out.println("VOREMOTHMECHANICS: Starting Voremoth mechanic for " + voremoth);
        activeVoremoth = voremoth;
        
        cycleTimer = 0;
        startNewCycle(null);
    }
    

    private static void startNewCycle(MinecraftServer server) {
        System.out.println("VOREMOTHMECHANICS: Starting new Voremoth mechanic cycle. Active beams: " + ACTIVE_BEAMS.size());
        ACTIVE_BEAMS.clear();
        cycleTimer = 0;

        if (activeVoremoth == null) return;

        // pick random color to display
        BeamColor[] colors = BeamColor.values();
        REQUIRED_COLOR = colors[activeVoremoth.getRandom().nextInt(colors.length)];

        // spawn 4 beams at random positions around the boss
        BlockPos bossPos = activeVoremoth.blockPosition();
        for (int i = 0; i < 4; i++) {
            int offsetX = (activeVoremoth.getRandom().nextInt(51)) - 25;
            int offsetZ = (activeVoremoth.getRandom().nextInt(51)) - 25;
            BlockPos beamPos = bossPos.offset(offsetX, 0, offsetZ);
            BeamColor beamColor = colors[activeVoremoth.getRandom().nextInt(colors.length)];
            ACTIVE_BEAMS.put(beamPos, beamColor);
        }

        // send color prompt to all nearby players
        if (server != null) {
            for (ServerLevel level : server.getAllLevels()) {
                for (ServerPlayer player : level.players()) {
                    ServerPlayer livePlayer = level.getServer().getPlayerList().getPlayer(player.getUUID());
                    if (livePlayer != null && livePlayer.distanceTo(activeVoremoth) < 100) {
                        String message = switch (REQUIRED_COLOR) {
                            case RED -> "BLOOD";
                            case BLUE -> "POWER";
                            case WHITE -> "PURITY";
                        };
                        livePlayer.sendOverlayMessage(
                            Component.literal("Voremoth demands ")
                                .append(Component.literal(message)
                                    .withStyle(getColorStyle(REQUIRED_COLOR)))
                            //true // true = action bar, false = chat
                        );
                    }
                }
            }
        }
    }

    private static void spawnBeamParticles(ServerLevel level, BlockPos pos, BeamColor color) {
        SimpleParticleType particle = switch (color) {
            case RED -> ParticleTypes.FALLING_LAVA;
            case BLUE -> ParticleTypes.GLOW;
            case WHITE -> ParticleTypes.END_ROD;
        };


        for (int y = 0; y < 50; y++) {
            level.sendParticles(particle, 
                pos.getX() + 0.5, SURFACE_Y_LEVEL + y, pos.getZ() + 0.5,
                2, 0.3, 0, 0.3, 0);
        }
    }



    private static void checkPlayerInBeam(ServerPlayer player, ServerLevel level) {
        ServerPlayer livePlayer = level.getServer().getPlayerList().getPlayer(player.getUUID());
        if (livePlayer == null) return;
        for (Map.Entry<BlockPos, BeamColor> entry : ACTIVE_BEAMS.entrySet()) {
            BlockPos beamPos = entry.getKey();
            BeamColor beamColor = entry.getValue();

            double dist = Math.sqrt(
                Math.pow(livePlayer.getX() - beamPos.getX(), 2) +
                Math.pow(livePlayer.getZ() - beamPos.getZ(), 2)
            );

            if (dist < 2.0) {
                if (beamColor == REQUIRED_COLOR) {
                    if (damageTimer % 20 == 0 && activeVoremoth != null) {
                        //System.out.println("VOREMOTHIMMUNITY: Boss invulnerable to beacon damage: " + activeVoremoth.isInvulnerableTo(level, level.damageSources().source(ModDamageTypes.BEACON_DAMAGE)));
                        activeVoremoth.hurtServer(level, level.damageSources().source(ModDamageTypes.BEACON_DAMAGE), 5.0F);
                    }
                } else {
                    livePlayer.addEffect(new MobEffectInstance(
                        BuiltInRegistries.MOB_EFFECT.wrapAsHolder(RedDarknessEffect.RED_DARKNESS),
                        1200, 0
                    ));
                    player.sendOverlayMessage(Component.literal("Voremoth demands the blood of your allies")); // TODO add messaage
                }
            }
        }
    }

    private static Style getColorStyle(BeamColor color) {
        return switch (color) {
            case RED -> Style.EMPTY.withColor(ChatFormatting.RED);
            case BLUE -> Style.EMPTY.withColor(ChatFormatting.BLUE);
            case WHITE -> Style.EMPTY.withColor(ChatFormatting.WHITE);
        };
    }
}
