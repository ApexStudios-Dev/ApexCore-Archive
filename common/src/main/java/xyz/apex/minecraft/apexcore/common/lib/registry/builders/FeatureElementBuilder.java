package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import xyz.apex.minecraft.apexcore.common.lib.registry.entries.FeatureElementEntry;

public interface FeatureElementBuilder<P, T extends FeatureElement, R extends T, E extends FeatureElementEntry<R>, B extends FeatureElementBuilder<P, T, R, E, B>> extends Builder<P, T, R, E, B>
{
    B requiredFeatures(FeatureFlag... requiredFeatures);
}
