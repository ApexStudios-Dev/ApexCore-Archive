package xyz.apex.minecraft.apexcore.common.lib.event;

import com.google.common.collect.Lists;

import java.util.List;

final class EventTypeImpl<E extends Event> implements EventType<E>
{
    private final List<EventListener<E>> listeners = Lists.newLinkedList();

    EventTypeImpl()
    {
    }

    @Override
    public EventResult<E> invoke(E event)
    {
        listeners.forEach(listener -> listener.listen(event));
        return new EventResultImpl<>(event);
    }

    @Override
    public void addListener(EventListener<E> listener)
    {
        if(listeners.contains(listener)) return;
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<E> listener)
    {
        if(!listeners.contains(listener)) return;
        listeners.remove(listener);
    }
}
