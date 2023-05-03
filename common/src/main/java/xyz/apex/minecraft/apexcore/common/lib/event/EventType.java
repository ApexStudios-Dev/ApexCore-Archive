package xyz.apex.minecraft.apexcore.common.lib.event;

/**
 * Base interface for all event types.
 *
 * @param <E> Type of event.
 */
public sealed interface EventType<E extends Event> permits EventTypeImpl
{
    /**
     * Post the given event to all registered listeners.
     * <p>
     * Returns event result stating if event was cancelled or not.
     *
     * @param event Event to be posted.
     * @return Event result stating if event was cancelled or not.
     */
    EventResult<E> invoke(E event);

    /**
     * Registers a new listener to be invoked when event is posted.
     *
     * @param listener Listener to be invoked when event is posted.
     */
    void addListener(EventListener<E> listener);

    /**
     * Removes the given listener from the pool of listeners to be invoked when event is posted.
     *
     * @param listener Listener to be unregistered.
     */
    void removeListener(EventListener<E> listener);

    /**
     * Returns new event type.
     *
     * @param <E> Type of event.
     * @return New event type.
     */
    static <E extends Event> EventType<E> create()
    {
        return new EventTypeImpl<>();
    }
}
