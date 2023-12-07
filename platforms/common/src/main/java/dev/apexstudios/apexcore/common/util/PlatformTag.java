package dev.apexstudios.apexcore.common.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class PlatformTag<T>
{
    private static final Map<String, String> PLATFORM_TO_NAMESPACE = ImmutableMap.of("fabric", "c");

    private final ResourceKey<? extends Registry<T>> registryType;
    private final Map<String, TagKey<T>> platformTags;
    private final TagKey<T> defaultTag;

    private PlatformTag(Builder<T> builder)
    {
        registryType = builder.registryType;

        var helper = TagHelper.helper(registryType);
        defaultTag = helper.create(builder.defaultTagName);

        var platformTags = ImmutableMap.<String, TagKey<T>>builder();
        builder.tagNames.forEach((platform, tagName) -> platformTags.put(platform, helper.create(platformTagName(platform, tagName))));
        this.platformTags = platformTags.build();
    }

    public boolean contains(Holder<T> holder)
    {
        return holder.is(tag());
    }

    public boolean contains(@Nullable RegistryAccess registryAccess, @Nullable T element)
    {
        return TagHelper.isIn(registryAccess, tag(), element);
    }

    public boolean contains(@Nullable T element)
    {
        return contains(null, element);
    }

    public ResourceKey<? extends Registry<T>> registry()
    {
        return registryType;
    }

    public TagKey<T> defaultTag()
    {
        return defaultTag;
    }

    public TagKey<T> tag(String platform)
    {
        return platformTags.getOrDefault(platform, defaultTag);
    }

    public TagKey<T> tag()
    {
        return tag(ModLoader.get().id());
    }

    public Collection<TagKey<T>> platformTags()
    {
        return platformTags.values();
    }

    public Set<String> platforms()
    {
        return platformTags.keySet();
    }

    public ResourceLocation defaultLocation()
    {
        return defaultTag.location();
    }

    public ResourceLocation location(String platform)
    {
        return tag(platform).location();
    }

    public ResourceLocation location()
    {
        return tag().location();
    }

    public boolean isFor(ResourceKey<? extends Registry<?>> otherRegistryType)
    {
        return registryType == otherRegistryType;
    }

    public <E> Optional<PlatformTag<E>> cast(ResourceKey<? extends Registry<E>> registryType)
    {
        return isFor(registryType) ? Optional.of((PlatformTag<E>) this) : Optional.empty();
    }

    @Override
    public String toString()
    {
        return "PlatformTag[%s / %s]".formatted(registryType.location(), defaultTag.location());
    }

    public static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation defaultTagName)
    {
        return new Builder<>(registryType, defaultTagName);
    }

    public static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryType, String defaultTagNamespace, String defaultTagName)
    {
        return builder(registryType, new ResourceLocation(defaultTagNamespace, defaultTagName));
    }

    public static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryType, String defaultTagName)
    {
        return builder(registryType, platformTagName(defaultTagName));
    }

    public static ResourceLocation platformTagName(String platform, String tagName)
    {
        return new ResourceLocation(platformToNamespace(platform), tagName);
    }

    public static ResourceLocation platformTagName(String tagName)
    {
        return new ResourceLocation(platformToNamespace(), tagName);
    }

    public static String platformToNamespace(String platform)
    {
        return PLATFORM_TO_NAMESPACE.getOrDefault(platform, platform);
    }

    public static String platformToNamespace()
    {
        return platformToNamespace(ModLoader.get().id());
    }

    public static final class Builder<T>
    {
        private final Map<String, String> tagNames = Maps.newHashMap();
        private final ResourceKey<? extends Registry<T>> registryType;
        private final ResourceLocation defaultTagName;

        private Builder(ResourceKey<? extends Registry<T>> registryType, ResourceLocation defaultTagName)
        {
            this.registryType = registryType;
            this.defaultTagName = defaultTagName;
        }

        public Builder<T> withPlatform(String namespace, String tagName)
        {
            tagNames.put(namespace, tagName);
            return this;
        }

        public Builder<T> withFabric(String tagName)
        {
            return withPlatform("fabric", tagName);
        }

        public Builder<T> withFabric()
        {
            return withFabric(defaultTagName.getPath());
        }

        public Builder<T> withForge(String tagName)
        {
            return withPlatform("forge", tagName);
        }

        public Builder<T> withForge()
        {
            return withForge(defaultTagName.getPath());
        }

        public Builder<T> withNeoForge(String tagName)
        {
            return withPlatform("neoforge", tagName);
        }

        public Builder<T> withNeoForge()
        {
            return withNeoForge(defaultTagName.getPath());
        }

        public Builder<T> withForgeLike(String tagName)
        {
            return withForge(tagName).withNeoForge(tagName);
        }

        public Builder<T> withForgeLike()
        {
            return withForgeLike(defaultTagName.getPath());
        }

        public PlatformTag<T> create()
        {
            return new PlatformTag<>(this);
        }
    }
}
