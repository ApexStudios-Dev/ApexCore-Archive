package xyz.apex.minecraft.apexcore.fabric.platform;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

class FabricPlatformHolder implements PlatformHolder
{
    protected final FabricPlatform platform;

    protected FabricPlatformHolder(FabricPlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final Platform platform()
    {
        return platform;
    }
}
