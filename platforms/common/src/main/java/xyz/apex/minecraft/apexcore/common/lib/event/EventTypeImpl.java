/*
package xyz.apex.minecraft.apexcore.common.lib.event;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

final class EventTypeImpl<T extends Event> implements EventType<T>
{
    private final List<T> listeners = Lists.newLinkedList();
    private final List<T> invokable = Collections.unmodifiableList(listeners);
    private final Function<List<T>, T> invoker;

    EventTypeImpl(Function<List<T>, T> invoker)
    {
        this.invoker = invoker;
    }

    @Override
    public void addListener(T listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(T listener)
    {
        listeners.remove(listener);
    }

    @Override
    public T post()
    {
        return invoker.apply(invokable);
    }
}
*/
