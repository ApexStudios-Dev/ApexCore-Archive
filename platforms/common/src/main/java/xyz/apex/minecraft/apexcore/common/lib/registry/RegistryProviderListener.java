package xyz.apex.minecraft.apexcore.common.lib.registry;

import net.minecraft.data.DataProvider;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderLookup;

@FunctionalInterface
public interface RegistryProviderListener<P extends DataProvider, T, E extends RegistryEntry<T>>
{
    void accept(P provider, ProviderLookup lookup, E entry);
}
