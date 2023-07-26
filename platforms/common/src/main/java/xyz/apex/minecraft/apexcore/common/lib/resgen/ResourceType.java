package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public sealed interface ResourceType
{
    String folder();

    String extension();

    PackType packType();

    @Nullable ResourceType parent();

    static ResourceLocation compilePath(ResourceLocation resourceLocation, ResourceType resourceType, boolean withExtension)
    {
        var resourcePath = resourceLocation.withPrefix(resourceType.folder());
        return withExtension ? resourcePath.withSuffix(resourceType.extension()) : resourcePath;
    }

    static ResourceLocation compilePath(GeneratedResource resource, boolean withExtension)
    {
        return compilePath(resource.getResourceLocation(), resource.getResourceType(), withExtension);
    }

    static Path compileFilePath(PackOutput output, ResourceLocation resourceLocation, ResourceType resourceType)
    {
        var resourcePath = compilePath(resourceLocation, resourceType, true);
        var packTarget = switch(resourceType.packType()) {
            case CLIENT_RESOURCES -> PackOutput.Target.RESOURCE_PACK;
            case SERVER_DATA -> PackOutput.Target.DATA_PACK;
        };

        return output.getOutputFolder(packTarget).resolve(resourcePath.getNamespace()).resolve(resourcePath.getPath());
    }

    static Path compileFilePath(PackOutput output, GeneratedResource resource)
    {
        return compileFilePath(output, resource.getResourceLocation(), resource.getResourceType());
    }

    record Singleton(String folder, String extension, PackType packType) implements ResourceType
    {
        public Singleton
        {
            Validate.isTrue(!extension.isBlank() && extension.charAt(0) == '.');
            Validate.isTrue(!folder.isBlank() && folder.charAt(folder.length() - 1) == '/');
        }

        @Nullable
        @Override
        public ResourceType parent()
        {
            return null;
        }
    }

    record Parented(String folder, ResourceType parent) implements ResourceType
    {
        public Parented
        {
            Validate.isTrue(!folder.isBlank() && folder.charAt(folder.length() - 1) == '/');
        }

        @Override
        public String folder()
        {
            return parent.folder() + folder;
        }

        @Override
        public String extension()
        {
            return parent.extension();
        }

        @Override
        public PackType packType()
        {
            return parent.packType();
        }
    }
}
