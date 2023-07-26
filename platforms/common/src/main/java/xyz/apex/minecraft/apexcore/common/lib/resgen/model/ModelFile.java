package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.resgen.GeneratedResource;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceType;

import java.nio.file.Path;

public class ModelFile implements GeneratedResource
{
    private final ResourceLocation resourceLocation;
    private final ResourceType resourceType;

    @ApiStatus.Internal
    ModelFile(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        this.resourceLocation = resourceLocation;
        this.resourceType = resourceType;
    }

    @Override
    public final ResourceLocation getResourceLocation()
    {
        return resourceLocation;
    }

    @Override
    public final ResourceType getResourceType()
    {
        return resourceType;
    }

    @Override
    public final ResourceLocation getResourcePath(boolean withExtension)
    {
        return GeneratedResource.super.getResourcePath(withExtension);
    }

    @Override
    public final Path getResourceFilePath(PackOutput output)
    {
        return GeneratedResource.super.getResourceFilePath(output);
    }
}
