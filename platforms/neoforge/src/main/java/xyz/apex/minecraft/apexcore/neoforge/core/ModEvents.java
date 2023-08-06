package xyz.apex.minecraft.apexcore.neoforge.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import net.jodah.typetools.TypeResolver;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBusHelper;
import xyz.apex.minecraft.apexcore.neoforge.lib.EventBuses;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Super simple event management system, to allow listening to IModBus events for any mod.
 */
@ApiStatus.Internal
public final class ModEvents
{
    private static final Map<String, ModEvents> MOD_EVENTS_MAP = Maps.newConcurrentMap();

    private final Multimap<Class<? extends Event>, Consumer<? super Event>> eventListeners = MultimapBuilder.hashKeys().linkedListValues().build();
    private final Set<Class<? extends Event>> registeredEvents = Sets.newConcurrentHashSet();
    private final String ownerId;

    private ModEvents(String ownerId)
    {
        this.ownerId = ownerId;
    }

    // TODO: maybe support event priorities?

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E extends Event & IModBusEvent> void addListener(Class<E> eventType, Consumer<E> listener)
    {
        eventListeners.put(eventType, (Consumer) listener);

        EventBuses.addListener(ownerId, modBus -> {
            if(!registeredEvents.add(eventType)) return;
            EventBusHelper.addListener(modBus, EventPriority.HIGH, eventType, this::onEvent);
        });
    }

    public <E extends Event & IModBusEvent> void addListener(Consumer<E> listener)
    {
        addListener(eventType(listener), listener);
    }

    // We can ignore this as right now, currently there are no IModBus events that are also IGenericEvent types
    // actually, seems there is only 1 IGenericEvent, AttachCapabilitiesEvent.
    // public <E extends Event & IGenericEvent<T>, T> void addGenericListener()

    private <E extends Event & IModBusEvent> void onEvent(E event)
    {
        eventListeners.get(event.getClass()).forEach(listener -> listener.accept(event));
    }

    @SuppressWarnings("unchecked")
    private static <E extends Event> Class<E> eventType(Consumer<E> listener)
    {
        // same method as to how neoforge looks up the type internally
        // net.minecraftforge.eventbus.EventBus#getEventClass(Consumer) : L230
        // note to self: this is using a library neoforge includes, so we cant make use of this library in common/fabric code without including it ourselves
        var eventType = (Class<E>) TypeResolver.resolveRawArgument(Consumer.class, listener.getClass());

        if((Class<?>) eventType == TypeResolver.Unknown.class)
        {
            ApexCore.LOGGER.error("Failed to resolve handler for \"{}\"", listener);
            throw new IllegalStateException("Failed to resolve consumer event type: %s".formatted(listener));
        }

        return eventType;
    }

    public static ModEvents get(String ownerId)
    {
        return MOD_EVENTS_MAP.computeIfAbsent(ownerId, ModEvents::new);
    }

    // should be preferred to use String variant
    public static ModEvents active()
    {
        return get(ModLoadingContext.get().getActiveNamespace());
    }
}
