package xyz.apex.minecraft.apexcore.shared.event;

public abstract class Event
{
    public String getName()
    {
        return getClass().getSimpleName();
    }
}
