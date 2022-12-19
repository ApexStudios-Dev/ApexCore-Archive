package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@ApiStatus.Internal
public final class RootTransformBuilder implements Model.BlockModel.RootTransformBuilder<BlockModelBuilder>
{
    private final BlockModelBuilder parent;
    private final net.minecraftforge.client.model.generators.BlockModelBuilder.RootTransformBuilder forge;

    @ApiStatus.Internal
    RootTransformBuilder(BlockModelBuilder parent, net.minecraftforge.client.model.generators.BlockModelBuilder.RootTransformBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> translation(float x, float y, float z)
    {
        forge.translation(x, y, z);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> rotation(Quaternionf rotation)
    {
        forge.rotation(rotation);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> rotation(float x, float y, float z, boolean isDegrees)
    {
        forge.rotation(x, y, z, isDegrees);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> rightRotation(Quaternionf rotation)
    {
        forge.rightRotation(rotation);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> rightRotation(float x, float y, float z, boolean isDegrees)
    {
        forge.rightRotation(x, y, z, isDegrees);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> scale(float x, float y, float z)
    {
        forge.scale(x, y, z);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> origin(Vector3f origin)
    {
        forge.origin(origin);
        return this;
    }

    @Override
    public Model.BlockModel.RootTransformBuilder<BlockModelBuilder> origin(Model.BlockModel.TransformOrigin origin)
    {
        forge.origin(switch(origin) {
            case CENTER -> net.minecraftforge.client.model.generators.BlockModelBuilder.RootTransformBuilder.TransformOrigin.CENTER;
            case CORNER -> net.minecraftforge.client.model.generators.BlockModelBuilder.RootTransformBuilder.TransformOrigin.CORNER;
            case OPPOSING_CORNER -> net.minecraftforge.client.model.generators.BlockModelBuilder.RootTransformBuilder.TransformOrigin.OPPOSING_CORNER;
        });

        return this;
    }

    @Override
    public BlockModelBuilder end()
    {
        return parent;
    }
}
