package xyz.apex.minecraft.apexcore.shared.event;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class SimpleEvent<T> implements Event<T>
{
    private final List<Consumer<T>> listeners = Lists.newCopyOnWriteArrayList();

    @Override
    public void addListener(Consumer<T> listener)
    {
        synchronized(listeners)
        {
            listeners.add(listener);
        }
    }

    @SafeVarargs
    @Override
    public final void addAllListeners(Consumer<T>... listeners)
    {
        addAllListeners(Arrays.asList(listeners));
    }

    @Override
    public void addAllListeners(Collection<Consumer<T>> listeners)
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
    public void removeListener(Consumer<T> listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
        }
    }

    @Override
    public void removeAllOccurrences(Consumer<T> listener)
    {
        synchronized(listeners)
        {
            listeners.removeIf(listener::equals);
        }
    }

    @Override
    public void post(T event)
    {
        listeners.forEach(listener -> listener.accept(event));
    }
}
