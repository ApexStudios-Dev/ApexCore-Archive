package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

@ApiStatus.NonExtendable
public interface TickEvents
{
    EventType<StartClient> START_CLIENT = EventType.create(listeners -> () -> listeners.forEach(StartClient::handle));
    EventType<EndClient> END_CLIENT = EventType.create(listeners -> () -> listeners.forEach(EndClient::handle));
    EventType<StartRender> START_RENDER = EventType.create(listeners -> partialTick -> listeners.forEach(listener -> listener.handle(partialTick)));
    EventType<EndRender> END_RENDER = EventType.create(listeners -> partialTick -> listeners.forEach(listener -> listener.handle(partialTick)));
    EventType<StartServer> START_SERVER = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<EndServer> END_SERVER = EventType.create(listeners -> server -> listeners.forEach(listener -> listener.handle(server)));
    EventType<StartPlayer> START_PLAYER = EventType.create(listeners -> player -> listeners.forEach(listener -> listener.handle(player)));
    EventType<EndPlayer> END_PLAYER = EventType.create(listeners -> player -> listeners.forEach(listener -> listener.handle(player)));
    EventType<StartLivingEntity> START_LIVING_ENTITY = EventType.create(listeners -> living -> listeners.forEach(listener -> listener.handle(living)));

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartClient extends Event
    {
        void handle();
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface EndClient extends Event
    {
        void handle();
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartRender extends Event
    {
        void handle(float partialTick);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface EndRender extends Event
    {
        void handle(float partialTick);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartServer extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface EndServer extends Event
    {
        void handle(MinecraftServer server);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartPlayer extends Event
    {
        void handle(Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface EndPlayer extends Event
    {
        void handle(Player player);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface StartLivingEntity extends Event
    {
        void handle(LivingEntity living);
    }
}
