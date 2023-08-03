package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base interface for all feature element Registry Entries.
 *
 * @param <T> Type of Entry.
 */
public interface FeaturedEntry<T extends FeatureElement> extends RegistryEntry<T>
{
    /**
     * @return Set of all required {@linkplain FeatureFlag feature flags}.
     */
    @ApiStatus.NonExtendable
    default FeatureFlagSet requiredFeatures()
    {
        return value().requiredFeatures();
    }

    /**
     * @return {@code true} If underlying entry is enabled for given {@linkplain FeatureFlag feature flags}.
     */
    @ApiStatus.NonExtendable
    default boolean isEnabled(FeatureFlagSet enabledFeatures)
    {
        return value().isEnabled(enabledFeatures);
    }
}
