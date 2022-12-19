package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@ApiStatus.Internal
public class ModelBuilder<T extends ModelBuilder<T> & Model.ModelBuilder<T>> implements Model.ModelBuilder<T>
{
    protected final T self = (T) this;
    protected final net.minecraftforge.client.model.generators.ModelBuilder<?> forge;
    protected final Function<ResourceLocation, ModelFile> forgeModelFactory;
    protected final ExistingFileHelper existingFileHelper;
    private final List<ElementBuilder<T>> elements = Lists.newArrayList();
    private final TransformsBuilder<T> transforms;

    @ApiStatus.Internal
    protected ModelBuilder(net.minecraftforge.client.model.generators.ModelBuilder<?> forge, ExistingFileHelper existingFileHelper, Function<ResourceLocation, ModelFile> forgeModelFactory)
    {
        this.forge = forge;
        this.existingFileHelper = existingFileHelper;
        this.forgeModelFactory = forgeModelFactory;

        transforms = new TransformsBuilder<>(self, forge.transforms());
    }

    @Override
    public T parent(ResourceLocation modelPath)
    {
        forge.parent(forgeModelFactory.apply(modelPath));
        return self;
    }

    @Override
    public T texture(String key, String texture)
    {
        forge.texture(key, texture);
        return self;
    }

    @Override
    public T renderType(ResourceLocation renderType)
    {
        forge.renderType(renderType);
        return self;
    }

    @Override
    public Model.TransformsBuilder<T> transforms()
    {
        return transforms;
    }

    @Override
    public T ao(boolean ao)
    {
        forge.ao(ao);
        return self;
    }

    @Override
    public T guiLight(BlockModel.GuiLight guiLight)
    {
        forge.guiLight(guiLight);
        return self;
    }

    @Override
    public Model.ElementBuilder<T> element()
    {
        var result = new ElementBuilder<>(self, forge.element());
        elements.add(result);
        return result;
    }

    @Override
    public Model.ElementBuilder<T> element(int index)
    {
        return elements.get(index);
    }

    @Override
    public ResourceLocation getLocation()
    {
        return forge.getLocation();
    }

    @Override
    public ResourceLocation getUncheckedLocation()
    {
        return forge.getUncheckedLocation();
    }

    @Override
    public void assertExistence()
    {
        forge.assertExistence();
    }
}
