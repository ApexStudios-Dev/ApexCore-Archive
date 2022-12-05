package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ModPlatform
{
    PlatformGameRulesRegistry gameRules();
    PlatformRegistry registries();
    PlatformTierRegistry tierRegistry();
}
