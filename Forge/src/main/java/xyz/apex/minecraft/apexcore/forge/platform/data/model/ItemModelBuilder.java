package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

import java.util.List;
import java.util.function.Function;

@ApiStatus.Internal
public final class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> implements Model.ItemModel.ItemModelBuilder<ItemModelBuilder>
{
    private final List<OverrideBuilder> overrides = Lists.newArrayList();

    @ApiStatus.Internal
    public ItemModelBuilder(net.minecraftforge.client.model.generators.ItemModelBuilder forge, ExistingFileHelper existingFileHelper, Function<ResourceLocation, ModelFile> forgeModelFactory)
    {
        super(forge, existingFileHelper, forgeModelFactory);
    }

    @Override
    public Model.ItemModel.OverrideBuilder<ItemModelBuilder> override()
    {
        var result = new OverrideBuilder(this, ((net.minecraftforge.client.model.generators.ItemModelBuilder) forge).override(), existingFileHelper);
        overrides.add(result);
        return result;
    }

    @Override
    public Model.ItemModel.OverrideBuilder<ItemModelBuilder> override(int index)
    {
        return overrides.get(index);
    }
}
