package xyz.apex.forge.apexcore.lib.util;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import xyz.apex.java.utility.nullness.NonnullConsumer;

public final class ModEventBusHelper
{
	public static IEventBus getModEventBus()
	{
		return FMLJavaModLoadingContext.get().getModEventBus();
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, receiveCancelled, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), receiveCancelled, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), receiveCancelled, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, consumer);
	}

	private static <T extends ParallelDispatchEvent> NonnullConsumer<T> enqueueListener(NonnullConsumer<T> consumer)
	{
		return (T event) -> event.enqueueWork(() -> consumer.accept(event));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(priority, receiveCancelled, eventType, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		addListener(priority, receiveCancelled, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(EventPriority priority, NonnullConsumer<T> consumer)
	{
		addListener(priority, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(priority, eventType, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(receiveCancelled, eventType, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		addListener(receiveCancelled, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(Class<T> eventType, NonnullConsumer<T> consumer)
	{
		addListener(eventType, enqueueListener(consumer));
	}

	public static <T extends ParallelDispatchEvent> void addEnqueuedListener(NonnullConsumer<T> consumer)
	{
		addListener(enqueueListener(consumer));
	}
}
