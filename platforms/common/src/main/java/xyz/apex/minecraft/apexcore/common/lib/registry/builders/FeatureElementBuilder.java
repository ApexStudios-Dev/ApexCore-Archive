package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.FeatureElementEntry;

/**
 * FeatureElement builder used to construct new feature element like instances.
 *
 * @param <P> Type of parent element.
 * @param <T> Type of feature element like object.
 * @param <M> Type of builder manager.
 */
public non-sealed interface FeatureElementBuilder<P, T extends FeatureElement, R extends T, E extends FeatureElementEntry<R>, B extends FeatureElementBuilder<P, T, R, E, B, M>, M extends BuilderManager<M>> extends Builder<P, T, R, E, B, M>
{
    /**
     * Set the required feature flags for this element to be enabled.
     *
     * @param requiredFeatures Required feature flags for this element to be enabled.
     * @return This builder instance.
     */
    B requiredFeatures(FeatureFlag... requiredFeatures);
}
