package xyz.apex.minecraft.apexcore.shared.event;

public interface Event
{
    interface Cancelable extends Event
    {
        void setCanceled(boolean canceled);

        default void setCanceled()
        {
            setCanceled(true);
        }

        default void setEnabled()
        {
            setCanceled(false);
        }

        boolean isCanceled();
    }
}
