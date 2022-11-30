package xyz.apex.minecraft.apexcore.shared.event;

public enum EventResult
{
    /**
     * Pass along to other event listeners
     */
    PASS,

    /**
     * Stop other event listeners from receiving the event
     * but still allow the event to happen
     */
    INTERRUPT,

    /**
     * Cancel the event from happening
     * other event listeners can still re-enable the event by changing the result
     *
     * @implNote When running on the Forge side, if this event is backed by a Forge event type, that event must also be cancelable in order to be fully canceled
     */
    CANCEL;

    public static final EventResult DEFAULT = EventResult.PASS;

    public boolean isPass()
    {
        return this == PASS;
    }

    public boolean isInterrupt()
    {
        return this == INTERRUPT;
    }

    public boolean isCancel()
    {
        return this == CANCEL;
    }
}
