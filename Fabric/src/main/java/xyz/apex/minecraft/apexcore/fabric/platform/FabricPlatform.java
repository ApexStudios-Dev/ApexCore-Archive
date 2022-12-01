package xyz.apex.minecraft.apexcore.fabric.platform;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistry;

public final class FabricPlatform implements ModPlatform
{
    private final FabricGameRulesRegistry gameRulesRegistry = new FabricGameRulesRegistry(this);
    private final FabricRegistry registry = new FabricRegistry(this);

    @Override
    public PlatformGameRulesRegistry gameRules()
    {
        return gameRulesRegistry;
    }

    @Override
    public PlatformRegistry registries()
    {
        return registry;
    }
}