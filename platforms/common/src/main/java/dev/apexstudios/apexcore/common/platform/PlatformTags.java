package dev.apexstudios.apexcore.common.platform;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public interface PlatformTags
{
    TagKey<Block> blockRelocationNotSupported();

    default <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String path)
    {
        return TagKey.create(registryType, id(path));
    }

    default TagKey<Block> block(String path)
    {
        return tag(Registries.BLOCK, path);
    }

    default TagKey<Item> item(String path)
    {
        return tag(Registries.ITEM, path);
    }

    default ResourceLocation id(String path)
    {
        return new ResourceLocation(Platform.INSTANCE.id(), path);
    }

    static PlatformTags get()
    {
        return Platform.INSTANCE.tags();
    }

    static <T> Builder<T> builder(ResourceKey<? extends Registry<T>> registryType, String path)
    {
        return new Builder<>(registryType, path);
    }

    final class Builder<T>
    {
        private final ResourceKey<? extends Registry<T>> registryType;
        private final String path;
        private final Map<String, String> platformPaths = Maps.newHashMap();

        private Builder(ResourceKey<? extends Registry<T>> registryType, String path)
        {
            this.registryType = registryType;
            this.path = path;
        }

        public Builder<T> with(String platform, String path)
        {
            platformPaths.put(platform, path);
            return this;
        }

        public Builder<T> fabric(String path)
        {
            return with(Platform.ID_FABRIC, path);
        }

        public Builder<T> mcforge(String path)
        {
            return with(Platform.ID_MCFORGE, path);
        }

        public Builder<T> neoforge(String path)
        {
            return with(Platform.ID_NEOFORGE, path);
        }

        public TagKey<T> build()
        {
            var platformId = Platform.INSTANCE.id();
            var path = platformPaths.getOrDefault(platformId, this.path);
            return get().tag(registryType, path);
        }
    }
}
