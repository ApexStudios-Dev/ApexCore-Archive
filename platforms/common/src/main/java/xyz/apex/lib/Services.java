package xyz.apex.lib;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface Services
{
    /**
     * Loads a singleton service, throws exception if multiple implementations exist.
     *
     * @param serviceType Type of service to be loaded.
     * @param defaultImpl Default implementation if none could be found.
     * @return Loaded service instance.
     * @param <T> Type of service to be loaded
     */
    static <T> T singleton(Class<T> serviceType, @Nullable Supplier<@NotNull T> defaultImpl)
    {
        var services = list(serviceType);

        if(services.isEmpty() && defaultImpl != null)
            return defaultImpl.get();

        if(services.size() != 1)
        {
            var serviceNames = services.stream().map(Object::getClass).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one implementation of %s on the classpath. Found: %s".formatted(serviceType.getName(), serviceNames));
        }

        return services.get(0);
    }

    /**
     * Loads a singleton service, throws exception if multiple implementations exist.
     *
     * @param serviceType Type of service to be loaded.
     * @return Loaded service instance.
     * @param <T> Type of service to be loaded
     */
    static <T> T singleton(Class<T> serviceType)
    {
        return singleton(serviceType, null);
    }

    /**
     * Loads all services matching the provided service type.
     *
     * @param serviceType Type of service to be loaded.
     * @return Stream of all services matching the provided service type.
     * @param <T> Type of service to be loaded.
     */
    static <T> Stream<T> stream(Class<T> serviceType)
    {
        return ServiceLoader.load(serviceType).stream().peek(service -> bootstrap(serviceType, service)).map(ServiceLoader.Provider::get);
    }

    /**
     * Loads all services matching the provided service type.
     *
     * @param serviceType Type of service to be loaded.
     * @return List of all services matching the provided service type.
     * @param <T> Type of service to be loaded.
     */
    static <T> List<T> list(Class<T> serviceType)
    {
        return stream(serviceType).toList();
    }

    private static <T> void bootstrap(Class<T> serviceType, ServiceLoader.Provider<T> serviceProvider)
    {
        ApexCore.LOGGER.debug("Instantiating {} for Service {}", serviceProvider.type().getName(), serviceType.getName());
    }
}
