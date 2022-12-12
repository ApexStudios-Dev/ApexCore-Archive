package xyz.apex.minecraft.apexcore.shared.event;

import java.util.function.Consumer;

public final class EventManager
{
    private final EventTree events = new EventTree();

    public <T extends Event> EventFunction<T> registerEvent(Consumer<T> function, Class<T> eventClass)
    {
        return registerEvent(function, eventClass, 0);
    }

    public <T extends Event> EventFunction<T> registerEvent(Consumer<T> function, Class<T> eventClass, int priority)
    {
        return events.add(function, eventClass, priority);
    }

    public <T extends Event> void unregisterEvent(EventFunction<T> functionData)
    {
        events.remove(functionData);
    }

    public boolean call(Event event)
    {
        events.execute(event);
        return event instanceof Cancellable cancellable && cancellable.isCancelled();
    }

    @Override
    public String toString()
    {
        return events.toString();
    }
}
