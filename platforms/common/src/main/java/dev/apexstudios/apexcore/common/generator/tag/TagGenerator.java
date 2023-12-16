package dev.apexstudios.apexcore.common.generator.tag;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.AbstractResourceGenerator;
import dev.apexstudios.apexcore.common.generator.ProviderType;
import dev.apexstudios.apexcore.common.generator.ResourceGenerator;
import dev.apexstudios.apexcore.common.util.PlatformTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;

import java.util.Map;
import java.util.Objects;

public final class TagGenerator<T> extends AbstractResourceGenerator<TagGenerator<T>>
{
    private static final Map<ResourceKey<? extends Registry<?>>, ProviderType<? extends TagGenerator<?>>> GENERATORS = Maps.newHashMap();

    private final ResourceKey<? extends Registry<T>> registryType;
    private final Registry<T> registry;
    private final Map<ResourceLocation, TagBuilder<T>> tags = Maps.newHashMap();
    private final PackOutput.PathProvider pathProvider;

    private TagGenerator(String ownerId, PackOutput output, ResourceKey<? extends Registry<T>> registryType)
    {
        super(ownerId, output);

        this.registryType = registryType;

        registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryType);
        pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, TagManager.getTagDir(registryType));
    }

    ResourceLocation registryName(T element)
    {
        return Objects.requireNonNull(registry.getKey(element));
    }

    @Override
    protected void generate(CachedOutput cache, HolderLookup.Provider registries)
    {
        tags.forEach((tagName, tagBuilder) -> ResourceGenerator.save(
                cache,
                TagFile.CODEC,
                new TagFile(tagBuilder.entries, tagBuilder.replace),
                pathProvider.json(tagName)
        ));
    }

    public TagBuilder<T> tag(TagKey<T> tag)
    {
        return tag(tag.location());
    }

    public TagBuilder<T> tag(ResourceLocation tagName)
    {
        return tags.computeIfAbsent(tagName, $ -> new TagBuilder<>(this));
    }

    public TagBuilder<T> tag(String namespace, String tagName)
    {
        return tag(new ResourceLocation(namespace, tagName));
    }

    public TagGenerator<T> platformTag(PlatformTag<T> tag)
    {
        tag.platformTags().forEach(t -> tag(t).addTag(tag.defaultTag()));
        return this;
    }

    public static <T> ProviderType<TagGenerator<T>> getOrRegister(ResourceKey<? extends Registry<T>> registryType)
    {
        return (ProviderType<TagGenerator<T>>) GENERATORS.computeIfAbsent(registryType, $ -> ProviderType.register(
                ApexCore.ID,
                "tags/%s".formatted(registryType.location().getPath()),
                (ownerId, output) -> new TagGenerator<>(ownerId, output, registryType)
        ));
    }
}
