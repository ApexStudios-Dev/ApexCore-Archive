package xyz.apex.minecraft.apexcore.forge.data;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractBlockBenchConverter implements DataProvider
{
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final DataGenerator generator;
    protected final ExistingFileHelper existingFileHelper;
    protected final String modId;
    protected final BlockModelProvider blockModels;
    protected final ItemModelProvider itemModels;

    private final Map<ResourceLocation, BlockModelBuilder> blockModelBuilders = Maps.newHashMap();
    private final Map<ResourceLocation, Path> blockModelInputPaths = Maps.newHashMap();
    private final Map<ResourceLocation, ItemModelBuilder> itemModelBuilders = Maps.newHashMap();
    private final Map<ResourceLocation, Path> itemModelInputPaths = Maps.newHashMap();

    protected AbstractBlockBenchConverter(DataGenerator generator, ExistingFileHelper existingFileHelper, @Nullable BlockModelProvider blockModels, @Nullable ItemModelProvider itemModels, String modId)
    {
        this.generator = generator;
        this.existingFileHelper = existingFileHelper;
        this.modId = modId;
        this.blockModels = blockModels == null ? dummyBlockModels(generator, existingFileHelper, modId) : blockModels;
        this.itemModels = itemModels == null ? dummyItemModels(generator, existingFileHelper, modId) : itemModels;
    }

    protected AbstractBlockBenchConverter(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        this(generator, existingFileHelper, null, null, modId);
    }

    protected AbstractBlockBenchConverter(DataGenerator generator, ExistingFileHelper existingFileHelper, @Nullable BlockModelProvider blockModels, String modId)
    {
        this(generator, existingFileHelper, blockModels, null, modId);
    }

    protected AbstractBlockBenchConverter(DataGenerator generator, ExistingFileHelper existingFileHelper, @Nullable ItemModelProvider itemModels, String modId)
    {
        this(generator, existingFileHelper, null, itemModels, modId);
    }

    protected AbstractBlockBenchConverter(GatherDataEvent event, String modId)
    {
        this(event.getGenerator(), event.getExistingFileHelper(), null, null, modId);
    }

    protected AbstractBlockBenchConverter(GatherDataEvent event, @Nullable BlockModelProvider blockModels, @Nullable ItemModelProvider itemModels, String modId)
    {
        this(event.getGenerator(), event.getExistingFileHelper(), blockModels, itemModels, modId);
    }

    protected AbstractBlockBenchConverter(GatherDataEvent event, @Nullable BlockModelProvider blockModels, String modId)
    {
        this(event.getGenerator(), event.getExistingFileHelper(), blockModels, null, modId);
    }

    protected AbstractBlockBenchConverter(GatherDataEvent event, @Nullable ItemModelProvider itemModels, String modId)
    {
        this(event.getGenerator(), event.getExistingFileHelper(), null, itemModels, modId);
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
    public final void run(CachedOutput output) throws IOException
    {
        blockModelBuilders.clear();
        itemModelBuilders.clear();

        convertModels();

        var outputDir = generator.getOutputFolder();

        for(var entry : blockModelBuilders.entrySet())
        {
            generateModel(output, outputDir, entry.getKey(), entry.getValue());
        }

        for(var entry : itemModelBuilders.entrySet())
        {
            generateModel(output, outputDir, entry.getKey(), entry.getValue());
        }
    }

    private <T extends ModelBuilder<T>> void generateModel(CachedOutput output, Path outputDir, ResourceLocation inputPath, T modelBuilder) throws IOException
    {
        var json = modelBuilder.toJson();
        var outputPath = modelBuilder.getUncheckedLocation();
        LOGGER.info("Generating converted BlockBench model '{}' -> '{}'", inputPath, outputPath);
        var filePath = buildModelPath(outputDir, outputPath);
        existingFileHelper.trackGenerated(outputPath, PackType.CLIENT_RESOURCES, ".json", "models");
        DataProvider.saveStable(output, json, filePath);
    }

    @Override
    public String getName()
    {
        return "BlockBench2Minecraft-Converter";
    }

    private Path buildModelPath(Path dir, ResourceLocation modelPath)
    {
        return dir
                .resolve("assets")
                .resolve(modelPath.getNamespace())
                .resolve("models")
                .resolve("%s.json".formatted(modelPath.getPath()))
        ;
    }

    private Path findInputPath(ResourceLocation modelPath)
    {
        for(var inputDir : generator.getInputFolders())
        {
            var filePath = buildModelPath(inputDir, modelPath);
            if(Files.exists(filePath)) return filePath;
        }

        throw new RuntimeException(new FileNotFoundException("Could not determine input model path from model name '%s'".formatted(modelPath)));
    }

    public static BlockModelProvider dummyBlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        return new BlockModelProvider(generator, modId, existingFileHelper) {
            @Override
            protected void registerModels()
            {
            }
        };
    }

    public static ItemModelProvider dummyItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper, String modId)
    {
        return new ItemModelProvider(generator, modId, existingFileHelper) {
            @Override
            protected void registerModels()
            {
            }
        };
    }
}
