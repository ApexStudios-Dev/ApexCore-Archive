package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ModelProvider<T extends ModelBuilder<T>> implements DataProvider
{
    protected final PackOutput packOutput;
    protected final String modId;
    protected final String folder;
    private final Function<ResourceLocation, T> modelFactory;
    private final Map<ResourceLocation, T> models = Maps.newHashMap();

    @ApiStatus.Internal
    protected ModelProvider(PackOutput packOutput, String modId, String folder, Function<ResourceLocation, T> modelFactory)
    {
        this.packOutput = packOutput;
        this.modId = modId;
        this.folder = StringUtils.appendIfMissing(folder, "/");
        this.modelFactory = modelFactory;
    }

    @ApiStatus.Internal
    protected ModelProvider(PackOutput packOutput, String modId, ResourceKey<? extends Registry<?>> registryType, Function<ResourceLocation, T> modelFactory)
    {
        this(packOutput, modId, registryType.location().getPath(), modelFactory);
    }

    protected void clear()
    {
        models.clear();
    }

    protected abstract void registerModels();

    // region: Helpers
    public final ResourceLocation extendWithFolder(ResourceLocation location)
    {
        return extendWith(location, folder);
    }

    public final String extendWithFolder(String path)
    {
        return extendWith(path, folder);
    }

    public final ResourceLocation removeFolder(ResourceLocation location)
    {
        return remove(location, folder);
    }

    public final String removeFolder(String path)
    {
        return remove(path, folder);
    }
    // endregion

    protected Path getModelPath(T model)
    {
        var modelLocation = removeFolder(model.getModelPath());

        // /assets/<model_namespace>/models/<folder>/<model_path>.json
        return packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                         .resolve(modelLocation.getNamespace())
                         .resolve("models")
                         .resolve(folder)
                         .resolve("%s.json".formatted(modelLocation.getPath()))
        ;
    }

    // region: Builders
    public final T builder(ResourceLocation modelPath)
    {
        return models.computeIfAbsent(modelPath, modelFactory);
    }

    public final T builder(String namespace, String path)
    {
        return builder(new ResourceLocation(namespace, extendWithFolder(path)));
    }

    public final T builder(String path)
    {
        return builder(new ResourceLocation(extendWithFolder(path)));
    }
    // endregion

    public final T withParent(ResourceLocation modelPath, ResourceLocation parent)
    {
        return builder(modelPath).parent(parent);
    }

    public final T withParent(ResourceLocation modelPath, ModelBuilder<?> parent)
    {
        return withParent(modelPath, parent.getModelPath());
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput cache)
    {
        clear();
        registerModels();
        return CompletableFuture.allOf(models.values().stream().map(builder -> DataProvider.saveStable(cache, builder.toJson(), getModelPath(builder))).toArray(CompletableFuture[]::new));
    }

    public static ResourceLocation extendWith(ResourceLocation location, CharSequence prefix, CharSequence... prefixes)
    {
        var path = location.getPath();

        if(StringUtils.startsWith(path, prefix)) return location;
        if(StringUtils.startsWithAny(path, prefixes)) return location;

        path = StringUtils.prependIfMissing(path, prefix, prefixes);
        return new ResourceLocation(location.getNamespace(), path);
    }

    public static String extendWith(String path, CharSequence prefix, CharSequence... prefixes)
    {
        if(path.contains(":"))
        {
            var index = path.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
            var namespace = ResourceLocation.DEFAULT_NAMESPACE;
            path = path.substring(index + 1);
            if(index >= 1) namespace = path.substring(0, index);
            path = StringUtils.prependIfMissing(path, prefix, prefixes);
            return "%s:%s".formatted(namespace, path);
        }

        return StringUtils.prependIfMissing(path, prefix, prefixes);
    }

    public static ResourceLocation remove(ResourceLocation location, String prefix)
    {
        var path = location.getPath();
        if(StringUtils.startsWith(path, prefix)) return new ResourceLocation(location.getNamespace(), StringUtils.remove(path, prefix));
        return location;
    }

    public static String remove(String path, String prefix)
    {
        if(path.contains(":"))
        {
            var index = path.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);
            var namespace = ResourceLocation.DEFAULT_NAMESPACE;
            path = path.substring(index + 1);
            if(index >= 1) namespace = path.substring(0, index);
            if(StringUtils.startsWith(path, prefix)) path = StringUtils.remove(path, prefix);
            return "%s:%s".formatted(namespace, path);
        }

        if(StringUtils.startsWith(path, prefix)) return StringUtils.remove(path, prefix);
        return path;
    }
}
