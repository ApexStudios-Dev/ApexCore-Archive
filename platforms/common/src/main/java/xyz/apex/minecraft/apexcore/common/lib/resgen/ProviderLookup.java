package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.data.DataProvider;

public interface ProviderLookup
{
    <P extends DataProvider> P lookup(ProviderType<P> providerType);
}
