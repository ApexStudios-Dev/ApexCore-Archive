package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceGenerators;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ModelProvider implements DataProvider
{
    private final PackOutput output;
    private final Map<ResourceLocation, ModelBuilder> models = Maps.newHashMap();
    private final ResourceType resourceType;
    protected boolean serializePlatformOnly = true;

    protected ModelProvider(PackOutput output, ResourceType resourceType)
    {
        this.output = output;
        this.resourceType = resourceType;
    }

    protected abstract void registerModels();

    public final ModelBuilder getBuilder(ResourceLocation modelPath)
    {
        return models.computeIfAbsent(modelPath, path -> new ModelBuilder(path, resourceType));
    }

    public final ModelBuilder getBuilder(String modelPath)
    {
        return getBuilder(new ResourceLocation(modelPath));
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput output)
    {
        registerModels();

        return CompletableFuture.allOf(models
                .values()
                .stream()
                .peek(model -> ResourceGenerators.resourceHelper().track(model))
                .map(model -> DataProvider.saveStable(
                        output,
                        model.toJson(serializePlatformOnly),
                        model.getResourceFilePath(this.output)
                ))
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName()
    {
        return "Model Provider";
    }
}
