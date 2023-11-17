package dev.apexstudios.apexcore.common.util;

import dev.apexstudios.apexcore.common.ApexCore;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Services
{
    static <T> T singleton(Class<T> serviceType, Supplier<T> defaultImpl)
    {
        var services = list(serviceType);

        if(services.isEmpty())
            return defaultImpl.get();

        if(services.size() != 1)
        {
            var serviceNames = services.stream().map(Object::getClass).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException("There should be exactly one implementation of %s on the classpath. Found: %s".formatted(serviceType.getName(), serviceNames));
        }

        return services.get(0);
    }

    static <T> T singleton(Class<T> serviceType)
    {
        var services = list(serviceType);

        if(services.size() == 1)
            return services.get(0);

        var serviceNames = services.stream().map(Object::getClass).map(Class::getName).collect(Collectors.joining(",", "[", "]"));
        throw new IllegalStateException("There should be exactly one implementation of %s on the classpath. Found: %s".formatted(serviceType.getName(), serviceNames));
    }

    static <T> List<T> list(Class<T> serviceType)
    {
        return stream(serviceType).toList();
    }

    static <T> Stream<T> stream(Class<T> serviceType)
    {
        return ServiceLoader.load(serviceType).stream().map(provider -> bootstrap(serviceType, provider));
    }

    private static <T> T bootstrap(Class<T> serviceType, ServiceLoader.Provider<T> provider)
    {
        ApexCore.LOGGER.debug("Instantiating {} for Service {}", provider.type().getName(), serviceType.getName());

        var service = provider.get();

        if(service instanceof BootStrapped strapped)
            strapped.bootstrap();

        return service;
    }

    @FunctionalInterface
    interface BootStrapped
    {
        void bootstrap();
    }
}
