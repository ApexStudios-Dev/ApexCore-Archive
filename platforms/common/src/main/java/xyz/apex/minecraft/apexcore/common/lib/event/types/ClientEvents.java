package xyz.apex.minecraft.apexcore.common.lib.event.types;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

import java.util.List;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface ClientEvents
{
    EventType<PlayerInput> PLAYER_INPUT = EventType.create(listeners -> (player, input) -> listeners.forEach(listener -> listener.handle(player, input)));
    EventType<Starting> STARTING = EventType.create(listeners -> client -> listeners.forEach(listener -> listener.handle(client)));
    EventType<Started> STARTED = EventType.create(listeners -> client -> listeners.forEach(listener -> listener.handle(client)));
    EventType<Stopping> STOPPING = EventType.create(listeners -> client -> listeners.forEach(listener -> listener.handle(client)));
    EventType<Stopped> STOPPED = EventType.create(listeners -> client -> listeners.forEach(listener -> listener.handle(client)));
    EventType<Tooltip> TOOLTIP = EventType.create(listeners -> (stack, flag, tooltips) -> listeners.forEach(listener -> listener.handle(stack, flag, tooltips)));

    @ApiStatus.Internal
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface PlayerInput extends Event
    {
        void handle(Player player, Input input);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Starting extends Event
    {
        void handle(Minecraft client);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Started extends Event
    {
        void handle(Minecraft client);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Stopping extends Event
    {
        void handle(Minecraft client);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Stopped extends Event
    {
        void handle(Minecraft client);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Tooltip extends Event
    {
        void handle(ItemStack stack, TooltipFlag flag, List<Component> tooltips);
    }
}
