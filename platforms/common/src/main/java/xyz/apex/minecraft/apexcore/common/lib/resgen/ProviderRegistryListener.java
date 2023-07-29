package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.data.DataProvider;

@FunctionalInterface
public interface ProviderRegistryListener<P extends DataProvider, T, R extends T>
{
    void accept(P provider, ProviderLookup lookup, ProviderType.RegistryContext<T, R> context);
}
