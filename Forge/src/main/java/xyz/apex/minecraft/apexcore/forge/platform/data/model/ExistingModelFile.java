package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@ApiStatus.Internal
public final class ExistingModelFile extends ModelFile.ExistingModelFile implements Model.ModelFile
{
    @ApiStatus.Internal
    public ExistingModelFile(ResourceLocation location, ExistingFileHelper existingHelper)
    {
        super(location, existingHelper);
    }
}
