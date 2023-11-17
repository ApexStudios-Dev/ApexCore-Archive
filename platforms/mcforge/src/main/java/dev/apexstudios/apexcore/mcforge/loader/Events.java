package dev.apexstudios.apexcore.mcforge.loader;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

public interface Events
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
