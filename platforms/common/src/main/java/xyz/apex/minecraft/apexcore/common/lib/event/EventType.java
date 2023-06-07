package xyz.apex.minecraft.apexcore.common.lib.event;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.event.types.*;

import java.util.List;
import java.util.function.Function;

/**
 * Interface describing an event type.
 *
 * @param <T> Type of event.
 */
public sealed interface EventType<T extends Event> permits EventTypeImpl
{
    /**
     * Registers the given listener.
     *
     * @param listener Listener to be registered.
     */
    void addListener(T listener);

    /**
     * Unregisters the given listener.
     *
     * @param listener Listener to be unregistered.
     */
    void removeListener(T listener);

    /**
     * Posts this event type to all registered listeners, returning the result.
     *
     * @return Result after invoking all registered listeners.
     */
    T post();

    /**
     * Returns new event type, using given invoker to invoke all registered listeners.
     *
     * @param invoker Invoker used to invoke all listeners.
     * @param <T>     Type of event.
     * @return New event type.
     */
    static <T extends Event> EventType<T> create(Function<List<T>, T> invoker)
    {
        return new EventTypeImpl<>(invoker);
    }

    @ApiStatus.Internal
    static void bootstrap()
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> {
            ClientEvents.bootstrap();
            ScreenEvents.bootstrap();
        });

        EntityEvents.bootstrap();
        PlayerEvents.bootstrap();
        ServerEvents.bootstrap();
    }
}
