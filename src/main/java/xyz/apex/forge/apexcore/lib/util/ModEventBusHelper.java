package xyz.apex.forge.apexcore.lib.util;

import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ModEventBusHelper
{
	public static IEventBus getModEventBus()
	{
		return FMLJavaModLoadingContext.get().getModEventBus();
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, receiveCancelled, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(EventPriority priority, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), priority, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), receiveCancelled, eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), receiveCancelled, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), eventType, consumer);
	}

	public static <T extends Event & IModBusEvent> void addListener(NonNullConsumer<T> consumer)
	{
		EventBusHelper.addListener(getModEventBus(), consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, EventPriority priority, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, priority, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, receiveCancelled, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, boolean receiveCancelled, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, receiveCancelled, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, Class<T> eventType, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, eventType, consumer);
	}

	public static <T extends GenericEvent<? extends F> & IModBusEvent, F> void addGenericListener(Class<F> genericClassFilter, NonNullConsumer<T> consumer)
	{
		EventBusHelper.addGenericListener(getModEventBus(), genericClassFilter, consumer);
	}
}
