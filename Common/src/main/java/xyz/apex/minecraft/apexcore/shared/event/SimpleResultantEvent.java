package xyz.apex.minecraft.apexcore.shared.event;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class SimpleResultantEvent<T> implements ResultantEvent<T>
{
    private final List<Function<T, EventResult>> listeners = Lists.newCopyOnWriteArrayList();

    @Override
    public void addListener(Function<T, EventResult> listener)
    {
        synchronized(listeners)
        {
            listeners.add(listener);
        }
    }

    @Override
    public void addAllListeners(Collection<Function<T, EventResult>> listeners)
    {
        if(!listeners.isEmpty())
        {
            synchronized(this.listeners)
            {
                this.listeners.addAll(listeners);
            }
        }
    }

    @Override
    public void removeListener(Function<T, EventResult> listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
        }
    }

    @Override
    public void removeAllOccurrences(Function<T, EventResult> listener)
    {
        synchronized(listeners)
        {
            listeners.removeIf(listener::equals);
        }
    }

    @Override
    public boolean post(T event)
    {
        var result = EventResult.PASS;

        for(var listener : listeners)
        {
            result = listener.apply(event);
        }

        return result == EventResult.CANCEL;
    }
}
