package xyz.apex.minecraft.apexcore.forge.platform;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

public class ForgePlatformHolder implements PlatformHolder
{
    protected final ForgePlatform platform;

    protected ForgePlatformHolder(ForgePlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final Platform platform()
    {
        return platform;
    }
}
