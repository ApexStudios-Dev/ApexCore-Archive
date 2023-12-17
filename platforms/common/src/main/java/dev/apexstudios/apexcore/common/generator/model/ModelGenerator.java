package dev.apexstudios.apexcore.common.generator.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.AbstractResourceGenerator;
import dev.apexstudios.apexcore.common.generator.ProviderType;
import dev.apexstudios.apexcore.common.generator.ResourceGenerator;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class ModelGenerator<T, B extends ModelBuilder<B, T>> extends AbstractResourceGenerator<ModelGenerator<T, B>>
{
    public static final ProviderType<ModelGenerator<Block, BlockModelBuilder>> BLOCK = register(Registries.BLOCK, BlockModelBuilder::new);
    public static final ProviderType<ModelGenerator<Item, ItemModelBuilder>> ITEM = register(Registries.ITEM, ItemModelBuilder::new);

    private final Multimap<ResourceLocation, Consumer<B>> modelModifiers = HashMultimap.create();
    private final ResourceKey<? extends Registry<T>> registryType;
    private final BiFunction<ModelGenerator<T, B>, ResourceLocation, B> modelBuilderFactory;

    private ModelGenerator(String ownerId, PackOutput output, ResourceKey<? extends Registry<T>> registryType, BiFunction<ModelGenerator<T, B>, ResourceLocation, B> modelBuilderFactory)
    {
        super(ownerId, output);

        this.registryType = registryType;
        this.modelBuilderFactory = modelBuilderFactory;
    }

    public ResourceLocation registryName(T element)
    {
        return ((Registry<T>) BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryType)).getKey(element);
    }

    public ResourceLocation withModelDir(T element)
    {
        return withModelDir(registryName(element));
    }

    public ResourceLocation withModelDir(ResourceLocation location)
    {
        return withModelDir(registryType, location);
    }

    public String withModelDir(String location)
    {
        return withModelDir(registryType, location);
    }

    public String appendModelDirIfMissing(String path)
    {
        return appendModelDirIfMissing(registryType, path);
    }

    public ModelGenerator<T, B> model(ResourceLocation modelPath, Consumer<B> modifier)
    {
        modelModifiers.put(modelPath, modifier);
        return this;
    }

    public ModelGenerator<T, B> model(String ownerId, String modelPath, Consumer<B> modifier)
    {
        return model(new ResourceLocation(ownerId, modelPath), modifier);
    }

    public ModelGenerator<T, B> model(String modelPath, Consumer<B> modifier)
    {
        return model(new ResourceLocation(modelPath), modifier);
    }

    public ModelGenerator<T, B> model(T element, Consumer<B> modifier)
    {
        return model(withModelDir(element), modifier);
    }

    public ModelGenerator<T, B> model(Holder<T> holder, Consumer<B> modifier)
    {
        var unwrapped = holder.unwrap();
        unwrapped.ifLeft(registryKey -> model(registryKey, modifier));
        unwrapped.ifRight(element -> model(element, modifier));
        return this;
    }

    public ModelGenerator<T, B> model(DeferredHolder<T, ? super T> holder, Consumer<B> modifier)
    {
        return model(holder.registryKey(), modifier);
    }

    public ModelGenerator<T, B> model(ResourceKey<T> registryKey, Consumer<B> modifier)
    {
        return model(withModelDir(registryKey.location()), modifier);
    }

    @Override
    protected void generate(CachedOutput cache, HolderLookup.Provider registries)
    {
        var provider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");

        var modifiers = HashMultimap.<ResourceLocation, Consumer<B>>create();
        var generated = Sets.<ResourceLocation>newHashSet();

        do
        {
            modifiers.clear();
            modifiers.putAll(modelModifiers);
            modelModifiers.clear();
            generate(this, modifiers, generated, cache, provider);
        }
        while(!modelModifiers.isEmpty());
    }

    private static <T, B extends ModelBuilder<B, T>> void generate(ModelGenerator<T, B> generator, Multimap<ResourceLocation, Consumer<B>> modelModifiers, Set<ResourceLocation> generated, CachedOutput cache, PackOutput.PathProvider provider)
    {
        var modelPaths = Sets.newHashSet(modelModifiers.keys());
        modelPaths.removeAll(generated);

        for(var modelPath : modelPaths)
        {
            if(generated.add(modelPath))
            {
                var path = provider.json(modelPath);
                var builder = generator.modelBuilderFactory.apply(generator, modelPath);
                modelModifiers.removeAll(modelPath).forEach(modifier -> modifier.accept(builder));
                var json = builder.toJson();
                ResourceGenerator.save(cache, json, path);
            }
        }
    }

    private static <T, B extends ModelBuilder<B, T>> ProviderType<ModelGenerator<T, B>> register(ResourceKey<? extends Registry<T>> registryType, BiFunction<ModelGenerator<T, B>, ResourceLocation, B> modelBuilderFactory)
    {
        return ProviderType.register(ApexCore.ID, "models/%s".formatted(registryType.location().getPath()), (ownerId, output) -> new ModelGenerator<>(ownerId, output, registryType, modelBuilderFactory));
    }

    public static ResourceLocation withModelDir(ResourceKey<? extends Registry<?>> registryType, ResourceLocation location)
    {
        return location.withPath(path -> appendModelDirIfMissing(registryType, path));
    }

    public static String withModelDir(ResourceKey<? extends Registry<?>> registryType, String location)
    {
        if(location.indexOf(0) == '#')
            return location;

        var index = location.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);

        var path = index >= 0 ? location.substring(index + 1) : location;
        path = appendModelDirIfMissing(registryType, path);

        if(index >= 1)
            return "%s:%s".formatted(location.substring(0, index), path);

        return path;
    }

    public static String appendModelDirIfMissing(ResourceKey<? extends Registry<?>> registryType, String path)
    {
        return path.indexOf(0) == '0' ? path : StringUtils.prependIfMissing(path, registryType.location().getPath() + '/');
    }
}
