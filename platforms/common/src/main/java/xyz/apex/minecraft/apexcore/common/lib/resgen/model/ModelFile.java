package xyz.apex.minecraft.apexcore.common.lib.resgen.model;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public class ModelFile
{
    protected final ResourceLocation location;

    @ApiStatus.Internal
    ModelFile(ResourceLocation location)
    {
        this.location = location;
    }

    public final ResourceLocation getLocation()
    {
        return location;
    }
}
