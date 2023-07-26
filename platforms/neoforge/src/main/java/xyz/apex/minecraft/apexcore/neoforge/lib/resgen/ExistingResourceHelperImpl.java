package xyz.apex.minecraft.apexcore.neoforge.lib.resgen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ExistingResourceHelper;
import xyz.apex.minecraft.apexcore.common.lib.resgen.GeneratedResource;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ResourceType;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

@ApiStatus.Internal
public final class ExistingResourceHelperImpl implements ExistingResourceHelper
{
    @Nullable private ExistingFileHelper existingFileHelper = null;

    @ApiStatus.Internal
    public ExistingResourceHelperImpl()
    {
    }

    public void setExistingFileHelper(ExistingFileHelper existingFileHelper)
    {
        Validate.isTrue(this.existingFileHelper == null);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public boolean isEnabled()
    {
        return existingFileHelper != null && existingFileHelper.isEnabled();
    }

    @Override
    public void enable()
    {
    }

    @Override
    public void disable()
    {
    }

    @Override
    public boolean exists(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        return existingFileHelper != null && existingFileHelper.exists(resourceLocation, resourceType.packType(), resourceType.extension(), resourceType.folder());
    }

    @Override
    public boolean exists(GeneratedResource resource)
    {
        return exists(resource.getResourceLocation(), resource.getResourceType());
    }

    @Override
    public void validateExistence(ResourceLocation resourceLocation, ResourceType resourceType)
    {
        if(existingFileHelper == null || !existingFileHelper.isEnabled())
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
        if(existingFileHelper != null)
            existingFileHelper.trackGenerated(resourceLocation, resourceType.packType(), resourceType.extension(), resourceType.folder());
    }

    @Override
    public void track(GeneratedResource resource)
    {
        track(resource.getResourceLocation(), resource.getResourceType());
    }

    @Override
    public Resource getResource(ResourceLocation resourceLocation, ResourceType resourceType) throws FileNotFoundException
    {
        if(existingFileHelper == null)
            throw new FileNotFoundException("Missing required ExistingFileHelper!");
        return existingFileHelper.getResource(resourceLocation, resourceType.packType(), resourceType.extension(), resourceType.folder());
    }

    @Override
    public Resource getResource(GeneratedResource resource) throws FileNotFoundException
    {
        return getResource(resource.getResourceLocation(), resource.getResourceType());
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation, PackType packType)
    {
        return existingFileHelper == null ? Collections.emptyList() : existingFileHelper.getResourceStack(resourceLocation, packType);
    }
}
