package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

public interface GeneratedResource
{
    ResourceLocation getResourceLocation();

    ResourceType getResourceType();

    @ApiStatus.NonExtendable
    default ResourceLocation getResourcePath(boolean withExtension)
    {
        return ResourceType.compilePath(this, withExtension);
    }

    @ApiStatus.NonExtendable
    default Path getResourceFilePath(PackOutput output)
    {
        return ResourceType.compileFilePath(output, this);
    }
}
