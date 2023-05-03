package xyz.apex.minecraft.apexcore.common.lib.event;

/**
 * Interface representing a cancelable event.
 */
public interface CancelableEvent extends Event
{
    /**
     * Cancels this event.
     */
    void cancel();

    /**
     * @return True if this event was cancelled.
     */
    boolean isCancelled();

    /**
     * Returns true if the given event was cancelled.
     *
     * @param event Event to check.
     * @return True if event was cancelled.
     */
    static boolean wasCancelled(Event event)
    {
        return event instanceof CancelableEvent cancelable && cancelable.isCancelled();
    }
}
