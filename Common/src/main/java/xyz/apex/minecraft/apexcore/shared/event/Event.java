package xyz.apex.minecraft.apexcore.shared.event;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public interface Event<T>
{
    void addListener(Consumer<T> listener);

    default void addAllListeners(Consumer<T>... listeners)
    {
        addAllListeners(Arrays.asList(listeners));
    }

    void addAllListeners(Collection<Consumer<T>> listeners);

    void removeListener(Consumer<T> listener);

    void removeAllOccurrences(Consumer<T> listener);

    void post(T event);
}
