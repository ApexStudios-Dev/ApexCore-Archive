package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Direction;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@SuppressWarnings("rawtypes")
@ApiStatus.Internal
public final class RotationBuilder<T extends ModelBuilder<T>> implements Model.RotationBuilder<T>
{
    private final ElementBuilder<T> parent;
    private final net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder.RotationBuilder forge;

    @ApiStatus.Internal
    RotationBuilder(ElementBuilder<T> parent, net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder.RotationBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.RotationBuilder<T> origin(float x, float y, float z)
    {
        forge.origin(x, y, z);
        return this;
    }

    @Override
    public Model.RotationBuilder<T> axis(Direction.Axis axis)
    {
        forge.axis(axis);
        return this;
    }

    @Override
    public Model.RotationBuilder<T> angle(float angle)
    {
        forge.angle(angle);
        return this;
    }

    @Override
    public Model.RotationBuilder<T> rescale(boolean rescale)
    {
        forge.rescale(rescale);
        return this;
    }

    @Override
    public Model.ElementBuilder<T> end()
    {
        return parent;
    }
}
