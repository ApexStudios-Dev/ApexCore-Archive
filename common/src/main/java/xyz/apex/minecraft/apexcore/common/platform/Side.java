package xyz.apex.minecraft.apexcore.common.platform;

import org.jetbrains.annotations.Nullable;

public enum Side
{
    CLIENT,
    SERVER;

    public boolean is(@Nullable Side other)
    {
        return this == other;
    }

    public boolean isClient()
    {
        return is(CLIENT);
    }

    public boolean isServer()
    {
        return is(SERVER);
    }
}
