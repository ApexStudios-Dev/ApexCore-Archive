package xyz.apex.minecraft.apexcore.common.lib;

import com.google.errorprone.annotations.DoNotCall;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryApi;

import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApiStatus.Internal
public interface Services
{
    RegistryApi REGISTRIES = load(RegistryApi.class);
    GameData GAME_DATA = load(GameData.class);

    /**
     * Returns first implementation of given service type from classpath, or default if one is specified.
     *
     * @param serviceType Type of service to be loaded.
     * @param defaultImpl Optional default implementation to be used if no implementation could be found on the classpath.
     * @param <T>         Type of service to be loaded.
     * @return First implementation of given service type on classpath or default none could be found.
     * @throws IllegalStateException If no service implementation could be found on classpath and no default implementation was provided.
     */
    static <T extends Service> T load(Class<T> serviceType, @Nullable Supplier<T> defaultImpl)
    {
        var loaders = ServiceLoader.load(serviceType).stream().toList();

        if(loaders.isEmpty() && defaultImpl != null) return bootstrap(serviceType, defaultImpl);

        if(loaders.size() != 1)
        {
            var names = loaders.stream().map(ServiceLoader.Provider::type).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("Expected only 1 %s ServiceLoader on the classpath, Found %s!".formatted(RegistryApi.class.getName(), names));
        }

        return bootstrap(serviceType, () -> loaders.get(0).get());
    }

    /**
     * Returns first implementation of given service type from classpath.
     *
     * @param serviceType Type of service to be loaded.
     * @param <T>         Type of service to be loaded.
     * @return First implementation of given service type on classpath.
     * @throws IllegalStateException If no service implementation could be found on classpath.
     */
    static <T extends Service> T load(Class<T> serviceType)
    {
        return load(serviceType, null);
    }

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }

    private static <T extends Service> T bootstrap(Class<T> serviceType, Supplier<T> serviceLoader)
    {
        var service = serviceLoader.get();
        ApexCore.LOGGER.debug("Instantiated and Bootstrapping {} for service {}", service.getClass().getName(), serviceType.getName());
        service.bootstrap();
        return service;
    }

    interface Service
    {
        /**
         * Invoked after service is loaded to bootstrap this service & initialize any required elements.
         */
        default void bootstrap()
        {
        }
    }
}
