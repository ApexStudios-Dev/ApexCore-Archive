package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class ModelProvider implements DataProvider
{
    private final PackOutput output;
    private final String folder;
    private final Map<ResourceLocation, ModelBuilder> models = Maps.newHashMap();
    protected boolean serializePlatformOnly = true;

    protected ModelProvider(PackOutput output, String folder)
    {
        this.output = output;
        this.folder = folder;
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

    @Override
    public final CompletableFuture<?> run(CachedOutput output)
    {
        registerModels();

        return CompletableFuture.allOf(models.values().stream().map(model -> {
            var path = this.output
                    .getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(model.location.getNamespace())
                    .resolve("models")
                    .resolve(folder)
                    .resolve("%s.json".formatted(model.location.getPath()));

            return DataProvider.saveStable(output, model.toJson(serializePlatformOnly), path);
        }).toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName()
    {
        return "Model Provider (%s)".formatted(folder);
    }
}
