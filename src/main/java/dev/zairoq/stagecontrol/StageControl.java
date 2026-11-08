package dev.zairoq.stagecontrol;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class StageControl implements ModInitializer {
    private static final String DENIED_MESSAGE = "tengan paciencia :)";
    private static final SuggestionProvider<CommandSourceStack> DIMENSION_SUGGESTIONS =
            (context, builder) -> {
                builder.suggest("nether");
                builder.suggest("end");
                return builder.buildFuture();
            };

    private static StageState state;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> state = StageState.load(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (state != null) {
                state.save(server);
                state = null;
            }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(literal("stage")
                        .then(literal("lock")
                                .then(argument("dimension", StringArgumentType.word())
                                        .suggests(DIMENSION_SUGGESTIONS)
                                        .executes(context -> setLock(context.getSource(),
                                                StringArgumentType.getString(context, "dimension"), true))))
                        .then(literal("unlock")
                                .then(argument("dimension", StringArgumentType.word())
                                        .suggests(DIMENSION_SUGGESTIONS)
                                        .executes(context -> setLock(context.getSource(),
                                                StringArgumentType.getString(context, "dimension"), false))))
                        .then(literal("status").executes(context -> showStatus(context.getSource())))));
    }

    public static boolean isLocked(ResourceKey<Level> dimension) {
        if (dimension.equals(Level.NETHER)) {
            return state == null || state.netherLocked;
        }
        if (dimension.equals(Level.END)) {
            return state == null || state.endLocked;
        }
        return false;
    }

    private static int setLock(CommandSourceStack source, String dimensionName, boolean locked) {
        if (!source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) {
            source.sendFailure(Component.literal(DENIED_MESSAGE));
            return 0;
        }
        if (state == null) {
            source.sendFailure(Component.literal("Stage Control is not ready."));
            return 0;
        }

        String dimension = dimensionName.toLowerCase(Locale.ROOT);
        if (!dimension.equals("nether") && !dimension.equals("end")) {
            source.sendFailure(Component.literal("Use: nether or end."));
            return 0;
        }

        if (dimension.equals("nether")) {
            state.netherLocked = locked;
        } else {
            state.endLocked = locked;
        }
        state.save(source.getServer());
        source.sendSuccess(() -> Component.literal(
                (locked ? "Locked " : "Unlocked ") + dimension + "."), true);
        return 1;
    }

    private static int showStatus(CommandSourceStack source) {
        if (!source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR)) {
            source.sendFailure(Component.literal(DENIED_MESSAGE));
            return 0;
        }
        if (state == null) {
            source.sendFailure(Component.literal("Stage Control is not ready."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(
                "Nether: " + status(state.netherLocked) + " | End: " + status(state.endLocked)), false);
        return 1;
    }

    private static String status(boolean locked) {
        return locked ? "locked" : "unlocked";
    }

    private static final class StageState {
        private boolean netherLocked = true;
        private boolean endLocked = true;

        private static StageState load(MinecraftServer server) {
            StageState loaded = new StageState();
            Path file = server.getWorldPath(LevelResource.ROOT).resolve("stage-control.properties");
            if (!Files.exists(file)) {
                return loaded;
            }

            Properties properties = new Properties();
            try (InputStream input = Files.newInputStream(file)) {
                properties.load(input);
                loaded.netherLocked = Boolean.parseBoolean(properties.getProperty("nether_locked", "true"));
                loaded.endLocked = Boolean.parseBoolean(properties.getProperty("end_locked", "true"));
            } catch (IOException exception) {
                StageControl.LOG_ERROR("Could not load stage-control.properties", exception);
            }
            return loaded;
        }

        private void save(MinecraftServer server) {
            Path file = server.getWorldPath(LevelResource.ROOT).resolve("stage-control.properties");
            Properties properties = new Properties();
            properties.setProperty("nether_locked", Boolean.toString(netherLocked));
            properties.setProperty("end_locked", Boolean.toString(endLocked));
            try {
                Files.createDirectories(file.getParent());
                try (OutputStream output = Files.newOutputStream(file)) {
                    properties.store(output, "Stage Control state");
                }
            } catch (IOException exception) {
                StageControl.LOG_ERROR("Could not save stage-control.properties", exception);
            }
        }
    }

    private static void LOG_ERROR(String message, Exception exception) {
        System.err.println("[stage_control] " + message + ": " + exception.getMessage());
    }
}
