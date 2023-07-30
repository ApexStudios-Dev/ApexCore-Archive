package xyz.apex.minecraft.apexcore.common.lib.registry.builder;

import net.minecraft.world.flag.FeatureFlag;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

public interface FeaturedBuilder<O extends AbstractRegistrar<O>, P, T, R extends T, B extends FeaturedBuilder<O, P, T, R, B, E>, E extends RegistryEntry<R>> extends Builder<O, P, T, R, B, E>
{
    @ApiStatus.NonExtendable
    B requiredFeatures(FeatureFlag... requiredFeatures);
}
