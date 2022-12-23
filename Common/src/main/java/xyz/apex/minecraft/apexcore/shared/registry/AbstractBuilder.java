package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Maps;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractBuilder<
        T,
        R extends T,
        B extends AbstractBuilder<T, R, B, E>,
        E extends RegistryEntry<R>
>
{
    private final String modId;
    private final String registryName;
    private final ResourceLocation internalName;
    private final ResourceKey<? extends Registry<T>> registryType;
    private final ResourceKey<R> registryKey;
    private final Supplier<R> supplier = Lazy.of(this::construct);

    private final Function<ResourceKey<R>, E> registryEntryFactory;
    private final Map<ResourceKey<? extends Registry<?>>, Supplier<? extends AbstractBuilder<?, ?, ?, ?>>> children = Maps.newHashMap();

    protected AbstractBuilder(ResourceKey<? extends Registry<T>> registryType, String modId, String registryName, Function<ResourceKey<R>, E> registryEntryFactory)
    {
        this.registryType = registryType;
        this.modId = modId;
        this.registryName = registryName;
        this.registryEntryFactory = registryEntryFactory;

        internalName = new ResourceLocation(modId, registryName);
        registryKey = ResourceKey.create((ResourceKey) registryType, internalName);
    }

    protected abstract R construct();

    protected final <T1, R1 extends T1, B1 extends AbstractBuilder<T1, R1, B1, E1>, E1 extends RegistryEntry<R1>> B child(ResourceKey<? extends Registry<T1>> registryType, BiFunction<String, String, B1> builderFactory)
    {
        children.put(registryType, () -> builderFactory.apply(modId, registryName));
        return (B) this;
    }

    protected final <T1> B removeChild(ResourceKey<? extends Registry<T1>> registryType)
    {
        children.remove(registryType);
        return (B) this;
    }

    public final String getModId()
    {
        return modId;
    }

    public final String getRegistryName()
    {
        return registryName;
    }

    public final ResourceLocation getInternalName()
    {
        return internalName;
    }

    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    public final ResourceKey<R> getRegistryKey()
    {
        return registryKey;
    }

    public final Supplier<R> asSupplier()
    {
        return supplier;
    }

    public final E register()
    {
        var entry = registryEntryFactory.apply(registryKey);
        var logger = Platform.INSTANCE.getLogger();
        logger.info("Registering {}: {}", registryType.location(), internalName);
        Platform.INSTANCE.registries().register(registryType, entry, supplier);

        if(!children.isEmpty())
        {
            logger.debug("Registering children for {}", internalName);
            children.forEach((key, value) -> value.get().register());
            children.clear();
        }

        return entry;
    }
}
