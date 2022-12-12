package xyz.apex.minecraft.apexcore.shared.event;

import java.util.function.Consumer;

public record EventFunction<T extends Event>(Consumer<T> function, Class<? extends Event> eventClass, int priority) implements Comparable<EventFunction<? extends Event>>
{
    @Override
    public int compareTo(EventFunction<? extends Event> other)
    {
        return Integer.compare(priority, other.priority);
    }
}
