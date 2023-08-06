package xyz.apex.minecraft.apexcore.neoforge.lib;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * Simple helper class to add missing method overloads for optional parameters to {@linkplain IEventBus}
 * <p>
 * We currently have a <a href="https://github.com/neoforged/Bus/pull/8">PR</a> open, to implement these directly into the EventBus API
 * <br>
 * If / When this PR gets merged treat these methods as deprecated and use the methods implemented via the PR instead.
 */
@ApiStatus.NonExtendable
public interface EventBusHelper
{
    static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, Consumer<T> consumer)
    {
        eventBus.addListener(priority, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        eventBus.addListener(priority, receiveCancelled, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addListener(priority, false, eventType, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addListener(priority, receiveCancelled, eventType, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addListener(EventPriority.NORMAL, receiveCancelled, eventType, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, Consumer<T> consumer)
    {
        eventBus.addListener(EventPriority.NORMAL, receiveCancelled, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addListener(EventPriority.NORMAL, false, eventType, consumer);
    }

    static <T extends Event> void addListener(IEventBus eventBus, Consumer<T> consumer)
    {
        eventBus.addListener(consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, priority, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, priority, false, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, EventPriority.NORMAL, receiveCancelled, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, EventPriority.NORMAL, receiveCancelled, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, Class<T> eventType, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, EventPriority.NORMAL, false, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, Consumer<T> consumer)
    {
        eventBus.addGenericListener(genericClassFilter, consumer);
    }
}
