package xyz.apex.minecraft.apexcore.common.lib.resgen.tag;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class TagBuilder<T>
{
    private final ResourceLocation tagName;
    private final Function<T, ResourceKey<T>> keyExtractor;
    private final List<TagEntry> tags = Lists.newLinkedList();
    private boolean replace = false;

    TagBuilder(ResourceLocation tagName, Function<T, ResourceKey<T>> keyExtractor)
    {
        this.tagName = tagName;
        this.keyExtractor = keyExtractor;
    }

    public ResourceKey<T> getKey(T element)
    {
        return keyExtractor.apply(element);
    }

    public TagBuilder<T> replace(boolean replace)
    {
        this.replace = true;
        return this;
    }

    public TagBuilder<T> add(TagEntry... entries)
    {
        Collections.addAll(tags, entries);
        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addElement(T... elements)
    {
        for(var element : elements)
        {
            add(TagEntry.element(getKey(element).location()));
        }

        return this;
    }

    public TagBuilder<T> addElement(ResourceLocation... registryNames)
    {
        for(var registryName : registryNames)
        {
            add(TagEntry.element(registryName));
        }

        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addElement(ResourceKey<T>... registryKeys)
    {
        for(var registryKey : registryKeys)
        {
            add(TagEntry.element(registryKey.location()));
        }

        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addOptionalElement(T... elements)
    {
        for(var element : elements)
        {
            add(TagEntry.optionalElement(getKey(element).location()));
        }

        return this;
    }

    public TagBuilder<T> addOptionalElement(ResourceLocation... registryNames)
    {
        for(var registryName : registryNames)
        {
            add(TagEntry.optionalElement(registryName));
        }

        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addOptionalElement(ResourceKey<T>... registryKeys)
    {
        for(var registryKey : registryKeys)
        {
            add(TagEntry.optionalElement(registryKey.location()));
        }

        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addTag(TagKey<T>... tags)
    {
        for(var tag : tags)
        {
            add(TagEntry.tag(tag.location()));
        }

        return this;
    }

    public TagBuilder<T> addTag(ResourceLocation... tagNames)
    {
        for(var tagName : tagNames)
        {
            add(TagEntry.tag(tagName));
        }

        return this;
    }

    @SafeVarargs
    public final TagBuilder<T> addOptionalTag(TagKey<T>... tags)
    {
        for(var tag : tags)
        {
            add(TagEntry.optionalTag(tag.location()));
        }

        return this;
    }

    public TagBuilder<T> addOptionalTag(ResourceLocation... tagNames)
    {
        for(var tagName : tagNames)
        {
            add(TagEntry.optionalTag(tagName));
        }

        return this;
    }

    ResourceLocation tagName()
    {
        return tagName;
    }

    List<TagEntry> entries()
    {
        return List.copyOf(tags);
    }

    @ApiStatus.Internal
    DataResult<JsonElement> toJson()
    {
        return TagFile.CODEC.encodeStart(JsonOps.INSTANCE, new TagFile(tags, replace));
    }
}
