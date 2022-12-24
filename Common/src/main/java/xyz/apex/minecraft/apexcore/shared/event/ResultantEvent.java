package xyz.apex.minecraft.apexcore.shared.event;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public interface ResultantEvent<T>
{
    void addListener(Function<T, EventResult> listener);

    default void addAllListeners(Function<T, EventResult>... listeners)
    {
        addAllListeners(Arrays.asList(listeners));
    }

    void addAllListeners(Collection<Function<T, EventResult>> listeners);

    void removeListener(Function<T, EventResult> listener);

    void removeAllOccurrences(Function<T, EventResult> listener);

    boolean post(T event);
}
