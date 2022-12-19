package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraftforge.client.model.generators.ModelFile;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@ApiStatus.Internal
public final class UncheckedModelFile extends ModelFile.UncheckedModelFile implements Model.ModelFile
{
    @ApiStatus.Internal
    public UncheckedModelFile(String location)
    {
        super(location);
    }
}
