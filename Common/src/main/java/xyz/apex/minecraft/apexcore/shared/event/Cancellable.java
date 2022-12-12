package xyz.apex.minecraft.apexcore.shared.event;

public interface Cancellable
{
    void setCancelled(boolean cancel);
    boolean isCancelled();
}
