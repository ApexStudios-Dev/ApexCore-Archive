package dev.apexstudios.apexcore.mcforge.loader;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;

import java.util.function.Consumer;

public interface McForgeEvents
{
    static <T extends Event> void addListener(EventPriority priority, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, priority, consumer);
    }

    static <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, priority, receiveCancelled, consumer);
    }

    static <T extends Event> void addListener(EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, priority, eventType, consumer);
    }

    static <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, priority, receiveCancelled, eventType, consumer);
    }

    static <T extends Event> void addListener(boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, receiveCancelled, eventType, consumer);
    }

    static <T extends Event> void addListener(boolean receiveCancelled, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, receiveCancelled, consumer);
    }

    static <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, eventType, consumer);
    }

    static <T extends Event> void addListener(Consumer<T> consumer)
    {
        Events.addListener(MinecraftForge.EVENT_BUS, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, receiveCancelled, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, receiveCancelled, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, receiveCancelled, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, receiveCancelled, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Class<T> eventType, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, eventType, consumer);
    }

    static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Consumer<T> consumer)
    {
        Events.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, consumer);
    }
}
