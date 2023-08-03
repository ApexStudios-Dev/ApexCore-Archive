package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.world.flag.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

/**
 * Base interface for all feature element Builders.
 *
 * @param <O> Type of Registrar.
 * @param <P> Type of Parent.
 * @param <T> Type of Registry.
 * @param <R> Type of Entry.
 * @param <B> Type of Builder.
 * @param <E> Type of RegistryEntry.
 */
public interface FeaturedBuilder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends FeaturedBuilder<O, P, T, R, B, E>, E extends RegistryEntry<R>> extends Builder<O, P, T, R, B, E>
{
    /**
     * Used to mark entry as requiring the given {@linkplain FeatureFlag feature flags}.
     *
     * @param requiredFeatures Feature flags to be required.
     * @return This Builder.
     */
    @ApiStatus.NonExtendable
    B requiredFeatures(FeatureFlag... requiredFeatures);
}
