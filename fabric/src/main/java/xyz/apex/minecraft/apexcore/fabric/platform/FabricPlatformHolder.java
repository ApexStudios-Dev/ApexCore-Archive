package xyz.apex.minecraft.apexcore.fabric.platform;

import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

public class FabricPlatformHolder implements PlatformHolder
{
    protected final FabricPlatform platform;

    protected FabricPlatformHolder(FabricPlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final FabricPlatform platform()
    {
        return platform;
    }
}
