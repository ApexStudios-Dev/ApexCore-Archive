package xyz.apex.minecraft.apexcore.shared.event;

public enum EventResult
{
    /**
     * Pass along to other event listeners
     */
    PASS,

    /**
     * Cancel the event from happening
     * other event listeners can still re-enable the event by changing the result
     *
     * @implNote When running on the Forge side, if this event is backed by a Forge event type, that event must also be cancelable in order to be fully canceled
     */
    CANCEL
}
