package xyz.apex.minecraft.apexcore.common.lib.event;

/**
 * Base interface for all event results.
 *
 * @param <E> Type of event.
 */
public sealed interface EventResult<E extends Event> permits EventResultImpl
{
    /**
     * @return True if the event was cancelled.
     */
    boolean wasCancelled();

    /**
     * @return Event instance that was posted.
     */
    E event();
}
