package xyz.apex.minecraft.apexcore.shared.data.providers;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

@ApiStatus.NonExtendable
public interface Tag<T> extends DataProvider
{
    Builder<T> tag(TagKey<T> tag);

    @ApiStatus.NonExtendable
    interface Builder<T>
    {
        // region: Add
        // region: RegistryKey
        Builder<T> add(ResourceKey<T> registryKey, boolean optional);

        default Builder<T> add(ResourceKey<T> registryKey)
        {
            return add(registryKey, false);
        }

        default Builder<T> add(ResourceKey<T>... registryKeys)
        {
            Arrays.stream(registryKeys).forEach(this::add);
            return this;
        }
        // endregion

        // region: RegistryName
        Builder<T> add(ResourceLocation registryName, boolean optional);

        default Builder<T> add(ResourceLocation registryName)
        {
            return add(registryName, false);
        }

        default Builder<T> add(ResourceLocation... registryNames)
        {
            Arrays.stream(registryNames).forEach(this::add);
            return this;
        }
        // endregion

        // region: Value
        Builder<T> add(T value, boolean optional);

        default Builder<T> add(T value)
        {
            return add(value, false);
        }

        default Builder<T> add(T... values)
        {
            Arrays.stream(values).forEach(this::add);
            return this;
        }
        // endregion

        // region: Tag
        Builder<T> add(TagKey<T> tag, boolean optional);

        default Builder<T> add(TagKey<T> tag)
        {
            return add(tag, false);
        }

        default Builder<T> add(TagKey<T>... tags)
        {
            Arrays.stream(tags).forEach(this::add);
            return this;
        }
        // endregion

        // region: TagEntry
        Builder<T> add(TagEntry tag, boolean optional);

        default Builder<T> add(TagEntry tag)
        {
            return add(tag, false);
        }

        default Builder<T> add(TagEntry... tags)
        {
            Arrays.stream(tags).forEach(this::add);
            return this;
        }
        // endregion
        // endregion

        // region: Remove
        // region: RegistryKey
        Builder<T> remove(ResourceKey<T> registryKey);

        default Builder<T> remove(ResourceKey<T>... registryKeys)
        {
            Arrays.stream(registryKeys).forEach(this::remove);
            return this;
        }
        // endregion

        // region: RegistryName
        Builder<T> remove(ResourceLocation registryName);

        default Builder<T> remove(ResourceLocation... registryNames)
        {
            Arrays.stream(registryNames).forEach(this::remove);
            return this;
        }
        // endregion

        // region: Value
        Builder<T> remove(T value);

        default Builder<T> remove(T... values)
        {
            Arrays.stream(values).forEach(this::remove);
            return this;
        }
        // endregion

        // region: TagKey
        Builder<T> remove(TagKey<T> tag);

        default Builder<T> remove(TagKey<T>... tags)
        {
            Arrays.stream(tags).forEach(this::remove);
            return this;
        }
        // endregion

        // region: TagEntry
        Builder<T> remove(TagEntry tag);

        default Builder<T> remove(TagEntry... tags)
        {
            Arrays.stream(tags).forEach(this::remove);
            return this;
        }

        // endregion
        // endregion

        // region: Replace
        Builder<T> replace(boolean replace);

        default Builder<T> replace()
        {
            return replace(true);
        }
        // endregion
    }

    @ApiStatus.NonExtendable
    interface ItemTag extends Tag<Item>
    {
        Tag<Item> copy(TagKey<Block> blockTag, TagKey<Item> itemTag);
    }
}
