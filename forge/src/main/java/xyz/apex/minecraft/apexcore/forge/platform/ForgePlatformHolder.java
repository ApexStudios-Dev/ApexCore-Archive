package xyz.apex.minecraft.apexcore.forge.platform;

import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

public class ForgePlatformHolder implements PlatformHolder
{
    protected final ForgePlatform platform;

    protected ForgePlatformHolder(ForgePlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final ForgePlatform platform()
    {
        return platform;
    }
}
