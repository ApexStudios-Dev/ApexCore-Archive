package xyz.apex.minecraft.apexcore.fabric.lib.resgen;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ExistingResourceHelper;
import xyz.apex.minecraft.apexcore.common.lib.resgen.GeneratedResource;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceType;

import java.io.FileNotFoundException;
import java.util.List;

@ApiStatus.Internal
public final class ExistingResourceHelperImpl implements ExistingResourceHelper
{
    private final MultiPackResourceManager clientResourceManager;
    private final MultiPackResourceManager serverResourceManager;
    private final Multimap<PackType, ResourceLocation> generatedResources = HashMultimap.create();
    private boolean enabled = true;

    @ApiStatus.Internal
    public ExistingResourceHelperImpl()
    {
        var clientResources = ImmutableList.<PackResources>builder();
        var serverResources = ImmutableList.<PackResources>builder();

        clientResourceManager = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, clientResources.build());
        serverResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, serverResources.build());
    }

    private ResourceManager getResourceManager(PackType packType)
    {
        return switch(packType) {
            case CLIENT_RESOURCES -> clientResourceManager;
            case SERVER_DATA -> serverResourceManager;
        };
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public void enable()
    {
        enabled = true;
    }

    @Override
    public void disable()
    {
        enabled = false;
    }

    @Override
    public boolean exists(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        if(!enabled)
            return true;

        var packType = resourceType.packType();
        var resourcePath = ResourceType.compilePath(resourceLocation, resourceType, true);
        return generatedResources.containsEntry(packType, resourcePath) || getResourceManager(packType).getResource(resourcePath).isPresent();
    }

    @Override
    public boolean exists(GeneratedResource resource)
    {
        if(!enabled)
            return true;

        var packType = resource.getResourceType().packType();
        var resourcePath = resource.getResourcePath(true);
        return generatedResources.containsEntry(packType, resourcePath) || getResourceManager(packType).getResource(resourcePath).isPresent();
    }

    @Override
    public void validateExistence(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        if(!enabled)
            return;

        Validate.isTrue(exists(resourceLocation, resourceType), "Missing required resource '%s'", ResourceType.compilePath(resourceLocation, resourceType, true));
    }

    @Override
    public void validateExistence(GeneratedResource resource)
    {
        validateExistence(resource.getResourceLocation(), resource.getResourceType());
    }

    @Override
    public void track(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        var packType = resourceType.packType();
        var resourcePath = ResourceType.compilePath(resourceLocation, resourceType, true);
        generatedResources.put(packType, resourcePath);
    }

    @Override
    public void track(GeneratedResource resource)
    {
        var packType = resource.getResourceType().packType();
        var resourcePath = resource.getResourcePath(true);
        generatedResources.put(packType, resourcePath);
    }

    @Override
    public Resource getResource(ResourceLocation resourceLocation, ResourceType resourceType) throws FileNotFoundException
    {
        var packType = resourceType.packType();
        var resourcePath = ResourceType.compilePath(resourceLocation, resourceType, true);
        return getResourceManager(packType).getResourceOrThrow(resourcePath);
    }

    @Override
    public Resource getResource(GeneratedResource resource) throws FileNotFoundException
    {
        var packType = resource.getResourceType().packType();
        var resourcePath = resource.getResourcePath(true);
        return getResourceManager(packType).getResourceOrThrow(resourcePath);
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation, PackType packType)
    {
        return getResourceManager(packType).getResourceStack(resourceLocation);
    }
}
