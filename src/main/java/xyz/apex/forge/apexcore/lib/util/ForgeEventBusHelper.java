package xyz.apex.forge.apexcore.lib.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;

import xyz.apex.java.utility.nullness.NonnullConsumer;

public final class ForgeEventBusHelper
{
	public static <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, priority, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(EventPriority priority, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, priority, consumer);
	}

	public static <T extends Event> void addListener(EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, priority, eventType, consumer);
	}

	public static <T extends Event> void addListener(boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event> void addListener(boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, receiveCancelled, consumer);
	}

	public static <T extends Event> void addListener(Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, eventType, consumer);
	}

	public static <T extends Event> void addListener(NonnullConsumer<T> consumer)
	{
		EventBusHelper.addListener(MinecraftForge.EVENT_BUS, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, priority, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, Class<T> eventType, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F>, F> void addGenericListener(Class<F> genericClassFilter, NonnullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, genericClassFilter, consumer);
	}
}
