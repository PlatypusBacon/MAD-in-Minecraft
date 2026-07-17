package bunger.group.tyler.command;

import bunger.group.tyler.data.StructureEventData;
import bunger.group.tyler.event.god.God;
import bunger.group.tyler.structure.StructurePlacer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.ServerLevelData;

public class SetupStructureCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {

            dispatcher.register(Commands.literal("setstructure")
                    .requires(src -> src.permissions().hasPermission(
                            new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS)
                    ))


                    .then(Commands.literal("origin")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setStructureBounds(pos, data.getStructureEnd());
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aStructure origin set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("end")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setStructureBounds(data.getStructureOrigin(), pos);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aStructure end set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("bed")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setBedPos(pos);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aStructure bed set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("painting")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setPaintingPos(pos);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aPainting position set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("info")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                        "§eOrigin: "    + data.getStructureOrigin() + "\n" +
                                                "§eEnd: "       + data.getStructureEnd()    + "\n" +
                                                "§eBed: "       + data.getBedPos()          + "\n" +
                                                "§ePainting: "  + data.getPaintingPos()     + "\n" +
                                                "§eEvent day: " + data.getEventDay()        + "\n" +
                                                "§eEntered: "   + data.hasBeenEntered()     + "\n" +
                                                "§eSpawn locked: " + data.isSpawnpointLocked() + "\n" +
                                                "§eTrueOrigin: " + data.getTrueOrigin()
                                ), false);
                                return 1;
                            }))

                    .then(Commands.literal("spawn")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                BlockPos pos = player.blockPosition();
                                StructurePlacer.place(level, pos, "squirrel_house");
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aSquirrel house spawned at " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("enter")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = player.level();
                                StructureEventData data = StructureEventData.get(level);
                                if (!data.hasBeenEntered()) {
                                    data.setEntered();
                                    data.setSpawnpointLocked();
                                    player.setRespawnPosition(
                                            new ServerPlayer.RespawnConfig(
                                                    LevelData.RespawnData.of(level.dimension(), data.getBedPos(), 0f, 0f),
                                                    true
                                            ),
                                            false
                                    );
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§aSimulated entry!"), true);
                                } else {
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§eAlready entered."), false);
                                }
                                return 1;
                            }))

                    .then(Commands.literal("nextday")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                ServerLevelData data = (ServerLevelData) level.getLevelData();
                                long currentDay = level.getOverworldClockTime() / 24000L;
                                long nextMorning = (currentDay + 1) * 24000L + 1000L;

                                data.setGameTime(nextMorning);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aSkipped to next day."), true);
                                return 1;
                            }))
                    .then(Commands.literal("skiptoend")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                ServerLevelData data1 = (ServerLevelData) level.getLevelData();
                                while (data.getEventDay() < 5) {
                                    data.advanceDay(data.getLastDayProcessed() + 1);
                                }
                                long currentDay = level.getOverworldClockTime() / 24000L;
                                long day5Sunset = currentDay * 24000L + 12500L;
                                data1.setGameTime(day5Sunset);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aSkipped to day 5 sunset!"), true);
                                return 1;
                            }))

                    .then(Commands.literal("reset")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                BlockPos origin   = data.getStructureOrigin();
                                BlockPos end      = data.getStructureEnd();
                                BlockPos bed      = data.getBedPos();
                                BlockPos painting = data.getPaintingPos();
                                data.reset();
                                data.setStructureBounds(origin, end);
                                data.setBedPos(bed);
                                data.setPaintingPos(painting);
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aEvent data reset!"), true);
                                return 1;
                            }))

                    .then(Commands.literal("findbed")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                BlockPos min = data.getStructureOrigin();
                                BlockPos max = data.getStructureEnd();

                                for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                                    var state = level.getBlockState(pos);
                                    if (state.getBlock() instanceof
                                            net.minecraft.world.level.block.BedBlock) {
                                        if (state.getValue(net.minecraft.world.level.block.BedBlock.PART)
                                                == net.minecraft.world.level.block.state.properties
                                                .BedPart.HEAD) {
                                            BlockPos found = pos.immutable();
                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal("§aBed head found at: " + found), false);
                                            return 1;
                                        }
                                    }
                                }
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§cNo bed found in bounds"), false);
                                return 1;
                            }))

                    .then(Commands.literal("trigger")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                BlockPos origin = data.getStructureOrigin();
                                var nearest = level.players().stream()
                                        .min((a, b) -> Double.compare(
                                                a.distanceToSqr(origin.getX(), origin.getY(), origin.getZ()),
                                                b.distanceToSqr(origin.getX(), origin.getY(), origin.getZ())));
                                if (nearest.isEmpty()) {
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal("§cNo players found near origin!"), false);
                                    return 0;
                                }
                                God.start(level, origin, (ServerPlayer) nearest.get());
                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aEvent triggered!"), true);
                                return 1;
                            }))

                    .then(Commands.literal("spawngod")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);

                                var look = player.getLookAngle();
                                var spawnPos = player.position().add(-look.x * 4.0, 0.0, -look.z * 4.0);

                                var god = bunger.group.tyler.entity.ModEntities.GOD.create(level,
                                        net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED);
                                if (god == null) return 0;

                                god.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                                god.setTrackedPlayer(player);
                                level.addFreshEntity(god);
                                data.setGodSpawned();

                                ctx.getSource().sendSuccess(
                                        () -> Component.literal("§aGod spawned!"), true);
                                return 1;
                            }))

                    .then(Commands.literal("doordebug")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                BlockPos doorBottom = StructurePlacer.getDoorPos(level);
                                BlockPos doorTop = doorBottom.above();
                                var stateBottom = level.getBlockState(doorBottom);
                                var stateTop    = level.getBlockState(doorTop);
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                        "§eTrueOrigin: " + data.getTrueOrigin() + "\n" +
                                                "§eRotation: " + data.getStructureRotation() + "\n" +
                                                "§eDoor bottom: " + doorBottom + " = " + stateBottom.getBlock() + "\n" +
                                                "§eDoor top:    " + doorTop    + " = " + stateTop.getBlock()
                                ), false);
                                return 1;
                            }))
            );
        });
    }
}