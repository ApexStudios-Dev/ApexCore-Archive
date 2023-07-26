package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.resources.ResourceLocation;

public class ModelFile
{
    private final ResourceLocation modelPath;

    public ModelFile(ResourceLocation modelPath)
    {
        this.modelPath = modelPath;
    }

    public ModelFile(String modelPath)
    {
        this(new ResourceLocation(modelPath));
    }

    public final ResourceLocation getModelPath()
    {
        return modelPath;
    }

    public final boolean isGenerated()
    {
        return this instanceof ModelBuilder;
    }
}
