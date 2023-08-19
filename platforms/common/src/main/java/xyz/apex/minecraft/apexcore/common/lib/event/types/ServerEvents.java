package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

@ApiStatus.NonExtendable
public interface ServerEvents
{
    EventType<Starting> STARTING = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<Started> STARTED = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<Stopping> STOPPING = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<Stopped> STOPPED = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<RegisterCommands> REGISTER_COMMANDS = EventType.create(listeners -> (dispatcher, environment, context) -> listeners.forEach(listener -> listener.handle(dispatcher, environment, context)));

    @ApiStatus.Internal
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Starting extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Started extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Stopping extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Stopped extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface RegisterCommands extends Event
    {
        void handle(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment, CommandBuildContext context);
    }
}
