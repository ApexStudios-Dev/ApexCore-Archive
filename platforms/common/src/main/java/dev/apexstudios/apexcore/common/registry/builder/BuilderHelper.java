package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

@FunctionalInterface
public interface BuilderHelper
{
    <T, R extends T, H extends DeferredHolder<T, R>> H register(ResourceKey<? extends Registry<T>> registryType, Supplier<H> holderFactory, Supplier<R> valueFactory);
}
