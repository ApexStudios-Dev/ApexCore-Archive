package xyz.apex.minecraft.apexcore.forge.platform.data;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.Tag;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public abstract class TagGenerator<T> extends DelegatedProvider<TagGenerator.DummyTagsProvider<T>> implements Tag<T>
{
    private final Map<TagsProvider.TagAppender<T>, TagBuilder<T>> builderMap = Maps.newHashMap();
    protected final Registry<T> registry;

    protected TagGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper fileHelper, Registry<T> registry)
    {
        super(packOutputSupplier, lookupProviderSupplier, modId, fileHelper);

        this.registry = registry;
    }

    public final ResourceLocation getRegistryName(T value)
    {
        return Objects.requireNonNull(registry.getKey(value));
    }

    @Override
    public final Builder<T> tag(TagKey<T> tag)
    {
        var appender = provider.tag(tag);
        return builderMap.computeIfAbsent(appender, $ -> new TagBuilder<>($, this::getRegistryName));
    }

    @ApiStatus.Internal
    public static final class TagBuilder<T> implements Builder<T>
    {
        private final TagsProvider.TagAppender<T> appender;
        private final Function<T, ResourceLocation> registryNameFunc;

        private TagBuilder(TagsProvider.TagAppender<T> appender, Function<T, ResourceLocation> registryNameFunc)
        {
            this.appender = appender;
            this.registryNameFunc = registryNameFunc;
        }

        @Override
        public Builder<T> add(ResourceKey<T> registryKey, boolean optional)
        {
            if(optional) appender.addOptional(registryKey.location());
            else appender.add(registryKey);
            return this;
        }

        @Override
        public Builder<T> add(ResourceLocation registryName, boolean optional)
        {
            if(optional) appender.addOptional(registryName);
            else appender.getInternalBuilder().addElement(registryName);
            return this;
        }

        @Override
        public Builder<T> add(T value, boolean optional)
        {
            var registryName = registryNameFunc.apply(value);
            return add(registryName, optional);
        }

        @Override
        public Builder<T> add(TagKey<T> tag, boolean optional)
        {
            if(optional) appender.addOptionalTag(tag.location());
            else appender.addTag(tag);
            return this;
        }

        @Override
        public Builder<T> add(TagEntry tag, boolean optional)
        {
            if(optional) appender.addOptionalTag(tag.getId());
            else appender.add(tag);
            return this;
        }

        @Override
        public Builder<T> remove(ResourceKey<T> registryKey)
        {
            appender.remove(registryKey);
            return this;
        }

        @Override
        public Builder<T> remove(ResourceLocation registryName)
        {
            appender.remove(registryName);
            return this;
        }

        @Override
        public Builder<T> remove(T value)
        {
            var registryName = registryNameFunc.apply(value);
            return remove(registryName);
        }

        @Override
        public Builder<T> remove(TagKey<T> tag)
        {
            appender.remove(tag);
            return this;
        }

        @Override
        public Builder<T> remove(TagEntry tag)
        {
            appender.remove(tag.getId());
            return this;
        }

        @Override
        public Builder<T> replace(boolean replace)
        {
            appender.replace(replace);
            return this;
        }
    }

    @ApiStatus.Internal
    public static final class ItemTagGenerator extends TagGenerator<Item> implements ItemTag
    {
        private final BlockTagGenerator blockTags;

        @ApiStatus.Internal
        ItemTagGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper fileHelper, BlockTagGenerator blockTags)
        {
            super(packOutputSupplier, lookupProviderSupplier, modId, fileHelper, BuiltInRegistries.ITEM);

            this.blockTags = blockTags;
        }

        @Override
        protected DummyTagsProvider<Item> createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            return new DummyTagsProvider<>(packOutputSupplier.get(), Registries.ITEM, lookupProviderSupplier.get(), modId, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.Provider pProvider)
                {
                    Generators.processDataGenerator(modId, ProviderTypes.ITEM_TAGS, ItemTagGenerator.this);
                }
            };
        }

        @Override
        public Tag<Item> copy(TagKey<Block> blockTag, TagKey<Item> itemTag)
        {
            var itemTagBuilder = provider.getOrCreateRawBuilder(itemTag);
            blockTags.provider.getOrCreateRawBuilder(blockTag).build().forEach(itemTagBuilder::add);
            return this;
        }
    }

    @ApiStatus.Internal
    public static final class BlockTagGenerator extends TagGenerator<Block>
    {
        @ApiStatus.Internal
        BlockTagGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper fileHelper)
        {
            super(packOutputSupplier, lookupProviderSupplier, modId, fileHelper, BuiltInRegistries.BLOCK);
        }

        @Override
        protected DummyTagsProvider<Block> createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
        {
            return new DummyTagsProvider<>(packOutputSupplier.get(), Registries.BLOCK, lookupProviderSupplier.get(), modId, existingFileHelper) {
                @Override
                protected void addTags(HolderLookup.Provider pProvider)
                {
                    Generators.processDataGenerator(modId, ProviderTypes.BLOCK_TAGS, BlockTagGenerator.this);
                }

                @Override
                public net.minecraft.tags.TagBuilder getOrCreateRawBuilder(TagKey<Block> pTag)
                {
                    return super.getOrCreateRawBuilder(pTag);
                }
            };
        }
    }

    @ApiStatus.Internal
    public static abstract class DummyTagsProvider<T> extends TagsProvider<T>
    {
        @ApiStatus.Internal
        protected DummyTagsProvider(PackOutput pOutput, ResourceKey<? extends Registry<T>> pRegistryKey, CompletableFuture<HolderLookup.Provider> pLookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper)
        {
            super(pOutput, pRegistryKey, pLookupProvider, modId, existingFileHelper);
        }

        @Override
        public net.minecraft.tags.TagBuilder getOrCreateRawBuilder(TagKey<T> pTag)
        {
            return super.getOrCreateRawBuilder(pTag);
        }
    }
}
