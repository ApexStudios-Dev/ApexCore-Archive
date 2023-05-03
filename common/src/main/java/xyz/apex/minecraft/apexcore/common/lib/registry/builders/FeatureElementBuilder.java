package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.FeatureElementEntry;

/**
 * FeatureElement builder used to construct new feature element like instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of feature element like object.
 */
public interface FeatureElementBuilder<P, T extends FeatureElement, R extends T, E extends FeatureElementEntry<R>, B extends FeatureElementBuilder<P, T, R, E, B>> extends Builder<P, T, R, E, B>
{
    /**
     * Set the required feature flags for this element to be enabled.
     *
     * @param requiredFeatures Required feature flags for this element to be enabled.
     * @return This builder instance.
     */
    B requiredFeatures(FeatureFlag... requiredFeatures);
}
