package dev.apexstudios.apexcore.common.registry.builder;

import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class NoConfigBuilder<O extends AbstractRegister<O>, P, T, R extends T, H extends DeferredHolder<T, R>> extends AbstractBuilder<O, P, T, R, H, NoConfigBuilder<O, P, T, R, H>>
{
    private final Supplier<R> valueFactory;

    @ApiStatus.Internal
    public NoConfigBuilder(O owner, P parent, ResourceKey<? extends Registry<T>> registryType, String registrationName, BiFunction<String, ResourceKey<T>, H> holderFactory, BuilderHelper helper, Supplier<R> valueFactory)
    {
        super(owner, parent, registryType, registrationName, holderFactory, helper);

        this.valueFactory = valueFactory;
    }

    @Override
    protected R createValue()
    {
        return valueFactory.get();
    }
}
