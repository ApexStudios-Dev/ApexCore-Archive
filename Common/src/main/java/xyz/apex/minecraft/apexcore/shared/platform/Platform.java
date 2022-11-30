package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.NonExtendable
public interface Platform
{
    ModPlatform INSTANCE = ServiceLoader.load(ModPlatform.class).findFirst().orElseThrow();

    static PlatformGameRulesRegistry gameRules()
    {
        return INSTANCE.gameRules();
    }

    static PlatformRegistry registries()
    {
        return INSTANCE.registries();
    }
}
