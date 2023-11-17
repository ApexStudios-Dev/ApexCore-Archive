package dev.apexstudios.apexcore.mcforge.loader;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Map;
import java.util.function.Consumer;

public final class ModEvents
{
    private static final Map<String, IEventBus> MOD_BUS_MAP = Maps.newConcurrentMap();
    private static final Multimap<String, Consumer<IEventBus>> LISTENERS = MultimapBuilder.hashKeys().linkedListValues().build();

    public static IEventBus register(String ownerId, IEventBus modBus)
    {
        if(MOD_BUS_MAP.putIfAbsent(ownerId, modBus) != null)
            throw new IllegalStateException("Attempt to register EventBus for mod '%s' twice!".formatted(ownerId));

        LISTENERS.removeAll(ownerId).forEach(listener -> listener.accept(modBus));
        return modBus;
    }

    public static IEventBus registerForJavaFML()
    {
        return register(ModLoadingContext.get().getActiveNamespace(), FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void whenRegistered(String ownerId, Consumer<IEventBus> listener)
    {
        var modBus = MOD_BUS_MAP.get(ownerId);

        if(modBus == null)
            LISTENERS.put(ownerId, listener);
        else
            listener.accept(modBus);
    }

    public static <T extends Event> void addListener(String modId, EventPriority priority, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, priority, consumer));
    }

    public static <T extends Event> void addListener(String modId, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, priority, receiveCancelled, consumer));
    }

    public static <T extends Event> void addListener(String modId, EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, priority, eventType, consumer));
    }

    public static <T extends Event> void addListener(String modId, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, priority, receiveCancelled, eventType, consumer));
    }

    public static <T extends Event> void addListener(String modId, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, receiveCancelled, eventType, consumer));
    }

    public static <T extends Event> void addListener(String modId, boolean receiveCancelled, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, receiveCancelled, consumer));
    }

    public static <T extends Event> void addListener(String modId, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, eventType, consumer));
    }

    public static <T extends Event> void addListener(String modId, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addListener(modBus, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, EventPriority priority, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, priority, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, priority, receiveCancelled, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, priority, eventType, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, priority, receiveCancelled, eventType, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, receiveCancelled, eventType, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, boolean receiveCancelled, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, receiveCancelled, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, Class<T> eventType, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, eventType, consumer));
    }

    public static <T extends GenericEvent<? extends F>, F> void addGenericListener(String modId, Class<F> genericClassFilter, Consumer<T> consumer)
    {
        whenRegistered(modId, modBus -> Events.addGenericListener(modBus, genericClassFilter, consumer));
    }
}
