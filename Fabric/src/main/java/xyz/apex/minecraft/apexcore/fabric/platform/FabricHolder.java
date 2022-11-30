package xyz.apex.minecraft.apexcore.fabric.platform;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformHolder;

public class FabricHolder implements PlatformHolder
{
    protected final FabricPlatform platform;

    protected FabricHolder(FabricPlatform platform)
    {
        this.platform = platform;
    }

    @Override
    public final ModPlatform platform()
    {
        return platform;
    }
}
