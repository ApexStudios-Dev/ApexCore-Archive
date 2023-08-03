package xyz.apex.minecraft.apexcore.common.lib.registry;

import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.entry.RegistryEntry;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
final class Registration<T, R extends T>
{
    private final ResourceKey<? extends Registry<T>> registryType;
    final ResourceLocation registryName;
    private final Supplier<R> entryFactory;
    final RegistryEntry<R> registryEntry;
    private final List<Consumer<R>> listeners = Lists.newArrayList();

    Registration(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, Supplier<R> entryFactory, Supplier<? extends RegistryEntry<R>> registryEntryFactory)
    {
        this.registryType = registryType;
        this.registryName = registryName;
        this.entryFactory = entryFactory;
        registryEntry = registryEntryFactory.get();
    }

    void register(RegistryHelper helper)
    {
        helper.register(registryType, registryName, entryFactory);
        registryEntry.bind();
        listeners.forEach(listener -> listener.accept(registryEntry.get()));
        listeners.clear();
    }

    void addListener(Consumer<R> listener)
    {
        listeners.add(listener);
    }
}
