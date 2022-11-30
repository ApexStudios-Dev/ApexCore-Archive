package xyz.apex.minecraft.apexcore.forge.platform;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformGameRulesRegistry;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistry;

public final class ForgePlatform implements ModPlatform
{
    private final ForgeGameRulesRegistry gameRulesRegistry = new ForgeGameRulesRegistry(this);
    private final ForgeRegistry registry = new ForgeRegistry(this);

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
