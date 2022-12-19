package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.renderer.block.model.ItemTransforms;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.Map;

@SuppressWarnings("rawtypes")
@ApiStatus.Internal
public final class TransformsBuilder<T extends ModelBuilder<T>> implements Model.TransformsBuilder<T>
{
    private final T parent;
    private final net.minecraftforge.client.model.generators.ModelBuilder.TransformsBuilder forge;
    private final Map<ItemTransforms.TransformType, TransformVecBuilder<T>> transforms = Maps.newLinkedHashMap();

    @ApiStatus.Internal
    TransformsBuilder(T parent, net.minecraftforge.client.model.generators.ModelBuilder.TransformsBuilder forge)
    {
        this.parent = parent;
        this.forge = forge;
    }

    @Override
    public Model.TransformVecBuilder<T> transform(ItemTransforms.TransformType transformType)
    {
        return transforms.computeIfAbsent(transformType, $ -> new TransformVecBuilder<>(this, forge.transform($)));
    }

    @Override
    public T end()
    {
        return parent;
    }
}
