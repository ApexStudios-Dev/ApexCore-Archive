package xyz.apex.minecraft.apexcore.forge.platform.data.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import xyz.apex.minecraft.apexcore.shared.data.providers.Model;

@ApiStatus.Internal
public final class OverrideBuilder implements Model.ItemModel.OverrideBuilder<ItemModelBuilder>
{
    private final ItemModelBuilder parent;
    private final net.minecraftforge.client.model.generators.ItemModelBuilder.OverrideBuilder forge;
    private final ExistingFileHelper existingFileHelper;

    @ApiStatus.Internal
    OverrideBuilder(ItemModelBuilder parent, net.minecraftforge.client.model.generators.ItemModelBuilder.OverrideBuilder forge, ExistingFileHelper existingFileHelper)
    {
        this.parent = parent;
        this.forge = forge;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public Model.ItemModel.OverrideBuilder<ItemModelBuilder> model(ResourceLocation modelPath)
    {
        forge.model(new ExistingModelFile(modelPath, existingFileHelper));
        return this;
    }

    @Override
    public Model.ItemModel.OverrideBuilder<ItemModelBuilder> predicate(ResourceLocation key, float value)
    {
        forge.predicate(key, value);
        return this;
    }

    @Override
    public ItemModelBuilder end()
    {
        return parent;
    }
}
