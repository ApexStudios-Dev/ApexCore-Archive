package xyz.apex.minecraft.apexcore.forge.data;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public abstract class BlockBenchModelConverter implements DataProvider
{
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final PackOutput packOutput;
    protected final ExistingFileHelper existingFileHelper;
    protected final String modId;
    protected final BlockModelProvider blockModels;
    protected final ItemModelProvider itemModels;
    private final Collection<Path> inputFolders;

    private final Map<ResourceLocation, BlockModelBuilder> blockModelBuilders = Maps.newHashMap();
    private final Map<ResourceLocation, Path> blockModelInputPaths = Maps.newHashMap();
    private final Map<ResourceLocation, ItemModelBuilder> itemModelBuilders = Maps.newHashMap();
    private final Map<ResourceLocation, Path> itemModelInputPaths = Maps.newHashMap();

    protected BlockBenchModelConverter(PackOutput packOutput, String modId, ExistingFileHelper existingFileHelper, Collection<Path> inputFolders)
    {
        this.packOutput = packOutput;
        this.existingFileHelper = existingFileHelper;
        this.inputFolders = inputFolders;
        this.modId = modId;

        blockModels = new BlockModelProvider(packOutput, modId, existingFileHelper) {
            @Override protected void registerModels() {}

            @Override
            public CompletableFuture<?> run(CachedOutput output)
            {
                return CompletableFuture.allOf();
            }
        };

        itemModels = new ItemModelProvider(packOutput, modId, existingFileHelper) {
            @Override protected void registerModels() {}

            @Override
            public CompletableFuture<?> run(CachedOutput output)
            {
                return CompletableFuture.allOf();
            }
        };
    }

    protected BlockBenchModelConverter(GatherDataEvent event, String modId)
    {
        this(event.getGenerator().getPackOutput(), modId, event.getExistingFileHelper(), getDataGeneratorConfig(event).getInputs());
    }

    protected abstract void convertModels();

    public final BlockModelBuilder blockModelBuilder(ResourceLocation inputPath, ResourceLocation outputPath)
    {
        return blockModelBuilders.computeIfAbsent(inputPath, $ ->
                BlockBenchModelDeserializer.blockModelBuilder(
                        outputPath,
                        blockModelInputPaths.computeIfAbsent(inputPath, this::findInputPath),
                        existingFileHelper
                )
        );
    }

    public final BlockModelBuilder blockModelBuilder(ResourceLocation modelPath)
    {
        return blockModelBuilder(modelPath, modelPath);
    }

    public final ItemModelBuilder itemModelBuilder(ResourceLocation inputPath, ResourceLocation outputPath)
    {
        return itemModelBuilders.computeIfAbsent(inputPath, $ ->
                BlockBenchModelDeserializer.itemModelBuilder(
                        outputPath,
                        itemModelInputPaths.computeIfAbsent(inputPath, this::findInputPath),
                        existingFileHelper
                )
        );
    }

    public final ItemModelBuilder itemModelBuilder(ResourceLocation modelPath)
    {
        return itemModelBuilder(modelPath, modelPath);
    }

    public final BlockModelProvider blockModels()
    {
        return blockModels;
    }

    public final ItemModelProvider itemModels()
    {
        return itemModels;
    }

    @Override
    public final CompletableFuture<?> run(CachedOutput output)
    {
        blockModelBuilders.clear();
        itemModelBuilders.clear();

        convertModels();
        var outputDir = packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK);

        return CompletableFuture.allOf(Stream
                .concat(blockModelBuilders.entrySet().stream(), itemModelBuilders.entrySet().stream())
                .map(entry -> generateModel(output, outputDir, entry.getKey(), entry.getValue()))
                .toArray(CompletableFuture[]::new)
        );
    }

    private CompletableFuture<?> generateModel(CachedOutput output, Path outputDir, ResourceLocation inputPath, ModelBuilder<?> modelBuilder)
    {
        var json = modelBuilder.toJson();
        var outputPath = modelBuilder.getUncheckedLocation();
        LOGGER.info("Generating converted BlockBench model '{}' -> '{}'", inputPath, outputPath);
        var filePath = buildModelPath(outputDir, outputPath, false);
        existingFileHelper.trackGenerated(outputPath, PackType.CLIENT_RESOURCES, ".json", "models");
        return DataProvider.saveStable(output, json, filePath);
    }

    @Override
    public String getName()
    {
        return "BlockBench2Minecraft-Converter";
    }

    private Path buildModelPath(Path dir, ResourceLocation modelPath, boolean input)
    {
        // input dirs need `assets/` prepending
        var rootDir = input ? dir.resolve("assets") : dir;
        return rootDir
                .resolve(modelPath.getNamespace())
                .resolve("models")
                .resolve("%s.json".formatted(modelPath.getPath()))
        ;
    }

    private Path findInputPath(ResourceLocation modelPath)
    {
        for(var inputDir : inputFolders)
        {
            var filePath = buildModelPath(inputDir, modelPath, true);
            if(Files.exists(filePath)) return filePath;
        }

        throw new RuntimeException(new FileNotFoundException("Could not determine input model path from model name '%s'".formatted(modelPath)));
    }

    public static GatherDataEvent.DataGeneratorConfig getDataGeneratorConfig(GatherDataEvent event)
    {
        try
        {
            var field = GatherDataEvent.class.getDeclaredField("config");
            field.setAccessible(true);
            return (GatherDataEvent.DataGeneratorConfig) field.get(event);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }
}
