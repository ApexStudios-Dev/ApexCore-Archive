package dev.apexstudios.apexcore.common.registry;

import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class Registration<T, R extends T>
{
    private final ResourceKey<? extends Registry<T>> registryType;
    private final Supplier<R> valueFactory;
    final DeferredHolder<T, R> delegate;
    private final List<Consumer<R>> listeners = Lists.newLinkedList();

    Registration(ResourceKey<? extends Registry<T>> registryType, Supplier<R> valueFactory, Supplier<? extends DeferredHolder<T, R>> delegateFactory)
    {
        this.registryType = registryType;
        this.valueFactory = valueFactory;
        delegate = delegateFactory.get();
    }

    void register(RegistrationHelper helper)
    {
        helper.register(registryType, delegate.registryKey, valueFactory.get());
        delegate.bind(true);
        listeners.forEach(listener -> listener.accept(delegate.value()));
        listeners.clear();
    }

    void addListener(Consumer<R> listener)
    {
        listeners.add(listener);
    }
}
