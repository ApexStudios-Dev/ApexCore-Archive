package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ModelProvider implements DataProvider
{
    private final PackOutput output;
    private final Map<ResourceLocation, ModelBuilder> models = Maps.newHashMap();
    protected boolean serializePlatformOnly = true;

    public ModelProvider(PackOutput output)
    {
        this.output = output;
    }

    protected abstract void registerModels();

    public final ModelBuilder getBuilder(ResourceLocation modelPath)
    {
        return models.computeIfAbsent(modelPath, ModelBuilder::new);
    }

    public final ModelBuilder getBuilder(String modelPath)
    {
        return getBuilder(new ResourceLocation(modelPath));
    }

    public final ModelFile existingModel(ResourceLocation modelPath)
    {
        return new ModelFile(modelPath);
    }

    public final ModelFile existingModel(String modelPath)
    {
        return existingModel(new ResourceLocation(modelPath));
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput output)
    {
        registerModels();

        return CompletableFuture.allOf(models
                .values()
                .stream()
                .map(model -> DataProvider.saveStable(
                        output,
                        model.toJson(serializePlatformOnly),
                        compileModelPath(model)
                ))
                .toArray(CompletableFuture[]::new)
        );
    }

    private Path compileModelPath(ModelFile model)
    {
        var modelPath = model.getModelPath();
        return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                     .resolve(modelPath.getNamespace())
                     .resolve("models")
                     .resolve("%s.json".formatted(modelPath.getPath()));
    }

    @Override
    public String getName()
    {
        return "Model Provider";
    }
}
