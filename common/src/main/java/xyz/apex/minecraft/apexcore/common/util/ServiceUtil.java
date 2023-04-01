package xyz.apex.minecraft.apexcore.common.util;

import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;

import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface ServiceUtil
{
    static <T> T lookup(Class<T> clazz, @Nullable Supplier<T> defaultImpl)
    {
        var providers = ServiceLoader.load(clazz).stream().toList();

        if(providers.isEmpty() && defaultImpl != null) return defaultImpl.get();
        else if(providers.size() != 1)
        {
            var names = providers.stream().map(ServiceLoader.Provider::type).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one implementation of %s on the classpath: Found: %s".formatted(clazz.getName(), names));
        }
        else
        {
            var provider = providers.get(0);
            ApexCore.LOGGER.debug("Instantiating {} for service {}", provider.type().getName(), clazz.getName());
            return provider.get();
        }
    }

    static <T> T lookup(Class<T> clazz)
    {
        return lookup(clazz, null);
    }
}
