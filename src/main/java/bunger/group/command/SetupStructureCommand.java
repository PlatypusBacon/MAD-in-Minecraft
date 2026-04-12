package bunger.group.command;

import bunger.group.data.StructureEventData;
import bunger.group.event.God;
import bunger.group.event.SundownWatcher;
import bunger.group.structure.StructurePlacer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import bunger.group.structure.StructurePlacer;

public class SetupStructureCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> {

            dispatcher.register(Commands.literal("setstructure")
                    .requires(src -> src.hasPermission(2))

                    .then(Commands.literal("origin")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setStructureBounds(pos, data.getStructureEnd());
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aStructure origin set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("end")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setStructureBounds(data.getStructureOrigin(), pos);
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aStructure end set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("bed")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setBedPos(pos);
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aStructure bed set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("painting")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructureEventData data = StructureEventData.get(level);
                                data.setPaintingPos(pos);
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aPainting position set to: " + pos), true);
                                return 1;
                            }))

                    .then(Commands.literal("info")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                ctx.getSource().sendSuccess(Component.literal(
                                        "§eOrigin: "    + data.getStructureOrigin() + "\n" +
                                                "§eEnd: "       + data.getStructureEnd()    + "\n" +
                                                "§eBed: "       + data.getBedPos()          + "\n" +
                                                "§ePainting: "  + data.getPaintingPos()     + "\n" +
                                                "§eEvent day: " + data.getEventDay()        + "\n" +
                                                "§eEntered: "   + data.hasBeenEntered()     + "\n" +
                                                "§eSpawn locked: " + data.isSpawnpointLocked()
                                ), false);
                                return 1;
                            }))

                    // spawn the structure at your current position
                    .then(Commands.literal("spawn")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructurePlacer.place(level, pos, "squirrel_house");
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aSquirrel house spawned at " + pos), true);
                                return 1;
                            }))

                    // simulate player entry for testing
                    .then(Commands.literal("enter")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                StructureEventData data = StructureEventData.get(level);
                                if (!data.hasBeenEntered()) {
                                    data.setEntered();
                                    data.setSpawnpointLocked();
                                    player.setRespawnPosition(
                                            level.dimension(),
                                            data.getBedPos(),
                                            0f, true, false);
                                    ctx.getSource().sendSuccess(
                                            Component.literal("§aSimulated entry!"), true);
                                } else {
                                    ctx.getSource().sendSuccess(
                                            Component.literal("§eAlready entered."), false);
                                }
                                return 1;
                            }))

                    // skip to next in-game day
                    .then(Commands.literal("nextday")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                long currentDay  = level.getDayTime() / 24000L;
                                long nextMorning = (currentDay + 1) * 24000L + 1000L;
                                level.setDayTime(nextMorning);
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aSkipped to next day."), true);
                                return 1;
                            }))

                    // skip straight to day 5 sunset
                    .then(Commands.literal("skiptoend")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                while (data.getEventDay() < 5) {
                                    data.advanceDay(data.getLastDayProcessed() + 1);
                                }
                                long day5Sunset = (level.getDayTime() / 24000L)
                                        * 24000L + 12500L;
                                level.setDayTime(day5Sunset);
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aSkipped to day 5 sunset!"), true);
                                return 1;
                            }))

                    // force trigger the God event
                    .then(Commands.literal("trigger")
                            .executes(ctx -> {
                                ServerLevel level = ctx.getSource().getLevel();
                                StructureEventData data = StructureEventData.get(level);
                                God.start(level, data.getStructureOrigin());
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aEvent triggered!"), true);
                                return 1;
                            }))

                    // reset all event state but keep positions
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
                                        Component.literal("§aEvent data reset!"), true);
                                return 1;
                            }))
                    .then(Commands.literal("spawn")
                            .executes(ctx -> {
                                ServerPlayer player = ctx.getSource().getPlayerOrException();
                                ServerLevel level = (ServerLevel) player.level;
                                BlockPos pos = player.blockPosition();
                                StructurePlacer.place(level, pos, "squirrel_house");
                                ctx.getSource().sendSuccess(
                                        Component.literal("§aSquirrel house spawned at " + pos), true);
                                return 1;
                            }))
            );
        });
    }
}