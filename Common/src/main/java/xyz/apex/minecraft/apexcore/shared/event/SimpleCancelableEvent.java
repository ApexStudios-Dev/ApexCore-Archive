package xyz.apex.minecraft.apexcore.shared.event;

public class SimpleCancelableEvent implements Event.Cancelable
{
    private boolean canceled = false;

    @Override
    public void setCanceled(boolean canceled)
    {
        this.canceled = canceled;
    }

    @Override
    public boolean isCanceled()
    {
        return canceled;
    }
}
