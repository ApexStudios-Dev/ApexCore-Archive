package xyz.apex.minecraft.apexcore.shared.event;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface EventType<T extends Event>
{
    void addListener(Consumer<T> listener);

    default void addAllListeners(Consumer<T>... listeners)
    {
        addAllListeners(Arrays.asList(listeners));
    }

    void addAllListeners(Collection<Consumer<T>> listeners);

    void removeListener(Consumer<T> listener);

    void removeAllOccurrences(Consumer<T> listener);

    boolean post(T event);

    static <T extends Event> EventType<T> simple()
    {
        return new Simple<>();
    }

    final class Simple<T extends Event> implements EventType<T>
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
        public boolean post(T event)
        {
            if(listeners.isEmpty()) return false;
            listeners.forEach(listener -> listener.accept(event));
            return event instanceof Event.Cancelable cancelable && cancelable.isCanceled();
        }
    }
}
