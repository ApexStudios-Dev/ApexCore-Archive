package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;
import xyz.apex.minecraft.apexcore.common.lib.resgen.state.BlockStateProvider;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ModelProvider implements DataProvider, Models
{
    public static final ProviderType<ModelProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "models"), ModelProvider::new, BlockStateProvider.PROVIDER_TYPE);

    private final ProviderType.ProviderContext context;
    private final Map<ResourceLocation, ModelBuilder> models = Maps.newHashMap();
    private boolean serializePlatformOnly = true; // TODO: expose this some how

    private ModelProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public ModelBuilder getBuilder(ResourceLocation modelPath)
    {
        return models.computeIfAbsent(modelPath, ModelBuilder::new);
    }

    public ModelBuilder getBuilder(String modelPath)
    {
        return getBuilder(new ResourceLocation(modelPath));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
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
        return context.packOutput()
                      .getOutputFolder(PackOutput.Target.RESOURCE_PACK)
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
