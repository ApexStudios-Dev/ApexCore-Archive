package xyz.apex.forge.apexcore.lib.util;

import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class EventBusHelper
{
	private static final boolean DEFAULT_RECEIVE_CANCELLED = false;
	private static final EventPriority DEFAULT_EVENT_PRIORITY = EventPriority.NORMAL;

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, priority, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		eventBus.addListener(priority, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, priority, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		eventBus.addListener(priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, NonNullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, priority, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, priority, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, NonNullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, consumer);
	}
}
