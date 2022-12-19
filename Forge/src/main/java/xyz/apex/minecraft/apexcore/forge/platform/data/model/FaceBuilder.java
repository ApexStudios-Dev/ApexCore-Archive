package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@SuppressWarnings("rawtypes")
@ApiStatus.Internal
public final class FaceBuilder<T extends ModelBuilder<T>> implements Model.FaceBuilder<T>
{
    private final ElementBuilder<T> parent;
    private final net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder.FaceBuilder forge;

    @ApiStatus.Internal
    FaceBuilder(ElementBuilder<T> parent, net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder.FaceBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.FaceBuilder<T> cullFace(@Nullable Direction face)
    {
        forge.cullface(face);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> tintIndex(int tintIndex)
    {
        forge.tintindex(tintIndex);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> texture(String texture)
    {
        forge.texture(texture);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> uvs(float minU, float minV, float maxU, float maxV)
    {
        forge.uvs(minU, minV, maxU, maxV);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> rotation(Model.FaceRotation rotation)
    {
        forge.rotation(switch(rotation) {
            case ZERO -> net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation.ZERO;
            case UPSIDE_DOWN -> net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation.UPSIDE_DOWN;
            case CLOCKWISE_90 -> net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation.CLOCKWISE_90;
            case COUNTERCLOCKWISE_90 -> net.minecraftforge.client.model.generators.ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90;
        });

        return this;
    }

    @Override
    public Model.FaceBuilder<T> emissivity(int emissivity)
    {
        forge.emissivity(emissivity);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> ao(boolean ao)
    {
        forge.ao(ao);
        return this;
    }

    @Override
    public Model.ElementBuilder<T> end()
    {
        return parent;
    }
}
