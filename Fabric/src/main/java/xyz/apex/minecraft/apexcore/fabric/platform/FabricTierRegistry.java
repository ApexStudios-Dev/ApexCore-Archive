package xyz.apex.minecraft.apexcore.fabric.platform;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformTierRegistry;

public final class FabricTierRegistry extends FabricHolder implements PlatformTierRegistry
{
    FabricTierRegistry(FabricPlatform platform)
    {
        super(platform);
    }
}
