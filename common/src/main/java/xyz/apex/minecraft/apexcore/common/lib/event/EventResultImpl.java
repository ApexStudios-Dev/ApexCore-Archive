package xyz.apex.minecraft.apexcore.common.lib.event;

public record EventResultImpl<E extends Event>(E event) implements EventResult<E>
{
    @Override
    public boolean wasCancelled()
    {
        return event instanceof CancelableEvent cancelable && cancelable.isCancelled();
    }
}
