package xyz.apex.minecraft.apexcore.forge.platform;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

public class ForgeHolder implements PlatformHolder
{
    protected final ForgePlatform platform;

    protected ForgeHolder(ForgePlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final ModPlatform platform()
    {
        return platform;
    }
}
