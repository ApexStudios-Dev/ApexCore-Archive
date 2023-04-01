package xyz.apex.minecraft.apexcore.common.registry.entry;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import xyz.apex.minecraft.apexcore.common.registry.RegistryEntry;

public interface FeatureElementEntry<T extends FeatureElement>
{
    @SuppressWarnings("unchecked")
    private RegistryEntry<T> self()
    {
        return (RegistryEntry<T>) this;
    }

    default FeatureFlagSet requiredFeatures()
    {
        return self().get().requiredFeatures();
    }

    default boolean isEnabled(FeatureFlagSet enabledFeatures)
    {
        return self().get().isEnabled(enabledFeatures);
    }
}
