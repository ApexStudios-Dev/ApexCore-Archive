package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface ClientConnectionEvents
{
    EventType<LoggingIn> LOGGING_IN = EventType.create(listeners -> (player, gameMode, connection) -> listeners.forEach(listener -> listener.handle(player, gameMode, connection)));
    EventType<LoggingOut> LOGGING_OUT = EventType.create(listeners -> connection -> listeners.forEach(listener -> listener.handle(connection)));

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface LoggingIn extends Event
    {
        void handle(LocalPlayer player, MultiPlayerGameMode gameMode, Connection connection);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface LoggingOut extends Event
    {
        void handle(@Nullable Connection connection);
    }
}
