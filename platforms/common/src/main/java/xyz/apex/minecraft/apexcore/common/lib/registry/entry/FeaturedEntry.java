package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.ApiStatus;

public interface FeaturedEntry<T extends FeatureElement> extends RegistryEntry<T>
{
    @ApiStatus.NonExtendable
    default FeatureFlagSet requiredFeatures()
    {
        return value().requiredFeatures();
    }

    @ApiStatus.NonExtendable
    default boolean isEnabled(FeatureFlagSet enabledFeatures)
    {
        return value().isEnabled(enabledFeatures);
    }
}
