package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
@ApiStatus.Internal
public final class ElementBuilder<T extends ModelBuilder<T>> implements Model.ElementBuilder<T>
{
    private final T parent;
    private final net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder forge;
    private final Map<Direction, FaceBuilder<T>> faces = Maps.newLinkedHashMap();
    @Nullable private RotationBuilder<T> rotation = null;

    @ApiStatus.Internal
    ElementBuilder(T parent, net.minecraftforge.client.model.generators.ModelBuilder.ElementBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.ElementBuilder<T> from(float x, float y, float z)
    {
        forge.from(x, y, z);
        return this;
    }

    @Override
    public Model.ElementBuilder<T> to(float x, float y, float z)
    {
        forge.to(x, y, z);
        return this;
    }

    @Override
    public Model.FaceBuilder<T> face(Direction face)
    {
        return faces.computeIfAbsent(face, $ -> new FaceBuilder<>(this, forge.face($)));
    }

    @Override
    public Model.RotationBuilder<T> rotation()
    {
        if(rotation == null) rotation = new RotationBuilder<>(this, forge.rotation());
        return rotation;
    }

    @Override
    public Model.ElementBuilder<T> shade(boolean shade)
    {
        forge.shade(shade);
        return this;
    }

    @Override
    public Model.ElementBuilder<T> faces(BiConsumer<Direction, Model.FaceBuilder<T>> action)
    {
        faces.forEach(action);
        return this;
    }

    @Override
    public T end()
    {
        return parent;
    }
}
