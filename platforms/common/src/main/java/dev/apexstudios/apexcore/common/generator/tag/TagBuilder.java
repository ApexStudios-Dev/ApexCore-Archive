package dev.apexstudios.apexcore.common.generator.tag;

import com.google.common.collect.Lists;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class TagBuilder<T>
{
    final List<TagEntry> entries = Lists.newArrayList();
    boolean replace = false;
    private final TagGenerator<T> tagGenerator;

    TagBuilder(TagGenerator<T> tagGenerator)
    {
        this.tagGenerator = tagGenerator;
    }

    public TagGenerator<T> end()
    {
        return tagGenerator;
    }

    private ResourceLocation registryName(T element)
    {
        return tagGenerator.registryName(element);
    }

    public TagBuilder<T> replace(boolean replace)
    {
        this.replace = replace;
        return this;
    }

    public TagBuilder<T> replace()
    {
        return replace(true);
    }

    public TagBuilder<T> add(TagEntry entry)
    {
        entries.add(entry);
        return this;
    }

    public TagBuilder<T> addElement(DeferredHolder<T, ? extends T> holder)
    {
        return addElement(holder.registryName());
    }

    public TagBuilder<T> addElement(Holder<T> holder)
    {
        return addElement(holder.unwrapKey().orElseThrow());
    }

    public TagBuilder<T> addElements(HolderSet<T> holders)
    {
        holders.forEach(this::addElement);
        return this;
    }

    public TagBuilder<T> addElement(ResourceKey<T> registryKey)
    {
        return addElement(registryKey.location());
    }

    public TagBuilder<T> addElement(String namespace, String elementName)
    {
        return addElement(new ResourceLocation(namespace, elementName));
    }

    public TagBuilder<T> addElement(ResourceLocation registryName)
    {
        return add(TagEntry.element(registryName));
    }

    // prefer to use one of the other methods where possible
    // this is a convenience method for vanilla elements
    @ApiStatus.Obsolete
    public TagBuilder<T> addElement(@Nullable T element)
    {
        return element == null ? this : addElement(registryName(element));
    }

    public TagBuilder<T> addOptionalElement(DeferredHolder<T, ? extends T> holder)
    {
        return addOptionalElement(holder.registryName());
    }

    public TagBuilder<T> addOptionalElement(Holder<T> holder)
    {
        return addOptionalElement(holder.unwrapKey().orElseThrow());
    }

    public TagBuilder<T> addOptionalElements(HolderSet<T> holders)
    {
        holders.forEach(this::addOptionalElement);
        return this;
    }

    public TagBuilder<T> addOptionalElement(ResourceKey<T> registryKey)
    {
        return addOptionalElement(registryKey.location());
    }

    public TagBuilder<T> addOptionalElement(String namespace, String elementName)
    {
        return addOptionalElement(new ResourceLocation(namespace, elementName));
    }

    public TagBuilder<T> addOptionalElement(ResourceLocation registryName)
    {
        return add(TagEntry.optionalElement(registryName));
    }

    // prefer to use one of the other methods where possible
    // this is a convenience method for vanilla elements
    @ApiStatus.Obsolete
    public TagBuilder<T> addOptionalElement(@Nullable T element)
    {
        return element == null ? this : addOptionalElement(registryName(element));
    }

    public TagBuilder<T> addTag(TagKey<T> tag)
    {
        return addTag(tag.location());
    }

    public TagBuilder<T> addTag(ResourceLocation tagName)
    {
        return add(TagEntry.tag(tagName));
    }

    public TagBuilder<T> addTag(String namespace, String tagName)
    {
        return addTag(new ResourceLocation(namespace, tagName));
    }

    public TagBuilder<T> addOptionalTag(TagKey<T> tag)
    {
        return addOptionalTag(tag.location());
    }

    public TagBuilder<T> addOptionalTag(String namespace, String tagName)
    {
        return addOptionalTag(new ResourceLocation(namespace, tagName));
    }

    public TagBuilder<T> addOptionalTag(ResourceLocation tagName)
    {
        return add(TagEntry.optionalTag(tagName));
    }
}
