package xyz.apex.minecraft.apexcore.shared.data;

import com.google.common.collect.Maps;

import net.minecraft.data.DataProvider;

import java.util.Map;

public final class ProviderType<T extends DataProvider>
{
    private static final Map<String, ProviderType<?>> PROVIDER_TYPES = Maps.newHashMap();

    private final String name;

    ProviderType(String name)
    {
        this.name = name;
        PROVIDER_TYPES.put(name, this);
    }

    public String getName()
    {
        return name;
    }
}
