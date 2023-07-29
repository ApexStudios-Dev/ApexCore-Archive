package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.data.DataProvider;

@FunctionalInterface
public interface ProviderListener<P extends DataProvider>
{
    void accept(P provider, ProviderLookup lookup);
}
