package xyz.apex.forge.apexcore.lib.util;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import xyz.apex.java.utility.nullness.NonnullConsumer;

public final class EventBusHelper
{
	private static final boolean DEFAULT_RECEIVE_CANCELLED = false;
	private static final EventPriority DEFAULT_EVENT_PRIORITY = EventPriority.NORMAL;

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, priority, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		eventBus.addListener(priority, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, priority, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		eventBus.addListener(priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends Event> void addListener(IEventBus eventBus, NonnullConsumer<T> consumer)
	{
		addListener(eventBus, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, priority, DEFAULT_RECEIVE_CANCELLED, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, priority, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		eventBus.addGenericListener(genericClassFilter, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(IEventBus eventBus, Class<F> genericClassFilter, NonnullConsumer<T> consumer)
	{
		addGenericListener(eventBus, genericClassFilter, DEFAULT_EVENT_PRIORITY, DEFAULT_RECEIVE_CANCELLED, consumer);
	}
}
