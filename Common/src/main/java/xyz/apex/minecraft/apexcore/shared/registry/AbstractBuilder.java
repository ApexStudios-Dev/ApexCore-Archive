package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import xyz.apex.minecraft.apexcore.shared.data.ProviderType;
import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractBuilder<T, R extends T, P, B extends AbstractBuilder<T, R, P, B, E>, E extends RegistryEntry<R>> implements Builder<T, R, P, B, E>
{
    protected final ResourceKey<? extends Registry<T>> registryType;
    protected final ResourceKey<T> registryKey;
    @Nullable protected final P parent;
    private final E registryEntry;

    private final Multimap<ProviderType<? extends TagsProvider<?>>, TagKey<?>> tagsByType = HashMultimap.create();

    protected AbstractBuilder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, @Nullable P parent, RegistryEntryFactoryEX<T, R, E> registryEntryFactory)
    {
        this.registryType = registryType;
        this.parent = parent;

        registryKey = ResourceKey.create(registryType, registryName);
        registryEntry = registryEntryFactory.create(registryType, registryName);
    }

    protected AbstractBuilder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation registryName, @Nullable P parent, RegistryEntryFactory<R, E> registryEntryFactory)
    {
        this(registryType, registryName, parent, createEntryFromSimpleFactory(registryType, registryEntryFactory));
    }

    protected abstract R construct();

    @Override
    public final E register()
    {
        Supplier<R> hookedRegistryValue = Lazy.of(() -> {
            var value = construct();
            var registry = RegistryEntry.getRegistryOrThrow(registryType);
            var holder = registry.wrapAsHolder(value);
            GamePlatform.EVENTS.call(new RegisterEvent<>(registry, registryKey, value));
            registryEntry.updateReference(value, holder);
            return value;
        });

        GamePlatform.INSTANCE.register(registryType, registryKey, hookedRegistryValue);
        return registryEntry;
    }

    @Override
    public final E get()
    {
        return registryEntry;
    }

    @Override
    public final P getParent()
    {
        return Objects.requireNonNull(parent);
    }

    @Override
    public final String getModId()
    {
        return getRegistryName().getNamespace();
    }

    @Override
    public final String getName()
    {
        return getRegistryName().getPath();
    }

    @Override
    public final ResourceLocation getRegistryName()
    {
        return registryKey.location();
    }

    @Override
    public final ResourceKey<? extends Registry<T>> getRegistryType()
    {
        return registryType;
    }

    @Override
    public final ResourceKey<T> getRegistryKey()
    {
        return registryKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public B tag(ProviderType<? extends TagsProvider<T>> providerType, TagKey<T>... tags)
    {
        if(!tagsByType.containsKey(providerType))
        {
            setData(providerType, (ctx, provider) -> tagsByType
                    .get(providerType)
                    .stream()
                    .map(t -> (TagKey<T>) t)
                    .map(provider::tag)
                    .forEach(b -> b.add(registryKey))
            );
        }

        tagsByType.putAll(providerType, Arrays.asList(tags));
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public B removeTag(ProviderType<? extends TagsProvider<T>> providerType, TagKey<T>... tags)
    {
        if(tagsByType.containsKey(providerType))
        {
            for(var tag : tags)
            {
                tagsByType.remove(providerType, tag);
            }
        }

        return (B) this;
    }

    private static <T, R extends T, E extends RegistryEntry<R>> RegistryEntryFactoryEX<T, R, E> createEntryFromSimpleFactory(ResourceKey<? extends Registry<T>> registryType, RegistryEntryFactory<R, E> entryFactory)
    {
        return (registryType1, registryName) -> {
            Validate.isTrue(registryType == registryType1);
            return entryFactory.create(registryName);
        };
    }
}
