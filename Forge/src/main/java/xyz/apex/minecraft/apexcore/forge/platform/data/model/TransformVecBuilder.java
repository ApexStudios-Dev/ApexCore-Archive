package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@SuppressWarnings("rawtypes")
@ApiStatus.Internal
public final class TransformVecBuilder<T extends ModelBuilder<T>> implements Model.TransformVecBuilder<T>
{
    private final TransformsBuilder<T> parent;
    private final net.minecraftforge.client.model.generators.ModelBuilder.TransformsBuilder.TransformVecBuilder forge;

    @ApiStatus.Internal
    TransformVecBuilder(TransformsBuilder<T> parent, net.minecraftforge.client.model.generators.ModelBuilder.TransformsBuilder.TransformVecBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.TransformVecBuilder<T> rotation(float x, float y, float z)
    {
        forge.rotation(x, y, z);
        return this;
    }

    @Override
    public Model.TransformVecBuilder<T> translation(float x, float y, float z)
    {
        forge.translation(x, y, z);
        return this;
    }

    @Override
    public Model.TransformVecBuilder<T> scale(float x, float y, float z)
    {
        forge.scale(x, y, z);
        return this;
    }

    @Override
    public Model.TransformVecBuilder<T> rightRotation(float x, float y, float z)
    {
        forge.rightRotation(x, y, z);
        return this;
    }

    @Override
    public Model.TransformsBuilder<T> end()
    {
        return parent;
    }
}
