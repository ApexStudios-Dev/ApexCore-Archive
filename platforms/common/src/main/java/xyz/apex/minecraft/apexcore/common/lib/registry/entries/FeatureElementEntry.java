package xyz.apex.minecraft.apexcore.common.lib.registry.entries;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;

/**
 * Registry entry which can be disabled using the FeatureFlagSet system.
 *
 * @param <T> Type of feature element.
 */
public interface FeatureElementEntry<T extends FeatureElement> extends RegistryEntry<T>
{
    /**
     * @return Required features for this element to be enabled.
     */
    default FeatureFlagSet requiredFeatures()
    {
        return map(FeatureElement::requiredFeatures).orElse(FeatureFlags.DEFAULT_FLAGS);
    }

    /**
     * Returns true if this feature element is enabled.
     *
     * @param enabledFeatures Currently enabled set of features.
     * @return True if this feature element is enabled.
     */
    default boolean isEnabled(FeatureFlagSet enabledFeatures)
    {
        return requiredFeatures().isSubsetOf(enabledFeatures);
    }
}
