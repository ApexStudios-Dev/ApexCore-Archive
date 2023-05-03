package xyz.apex.minecraft.apexcore.common.lib.event;

/**
 * Base interface for all event listeners.
 *
 * @param <E> Type of event.
 */
@FunctionalInterface
public interface EventListener<E extends Event>
{
    /**
     * Invoked by event type when event is posted.
     *
     * @param event Posted event.
     */
    void listen(E event);
}
