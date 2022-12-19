package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.function.Function;

@ApiStatus.Internal
public final class BlockModelBuilder extends ModelBuilder<BlockModelBuilder> implements Model.BlockModel.BlockModelBuilder<BlockModelBuilder>
{
    private final RootTransformBuilder rootTransformBuilder;

    @ApiStatus.Internal
    public BlockModelBuilder(net.minecraftforge.client.model.generators.BlockModelBuilder forge, ExistingFileHelper existingFileHelper, Function<ResourceLocation, ModelFile> forgeModelFactory)
    {
        super(forge, existingFileHelper, forgeModelFactory);

        rootTransformBuilder = new RootTransformBuilder(this, forge.rootTransform());
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> rootTransform()
    {
        return rootTransformBuilder;
    }
}
