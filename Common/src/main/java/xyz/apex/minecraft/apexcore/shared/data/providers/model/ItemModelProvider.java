package xyz.apex.minecraft.apexcore.shared.data.providers.model;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;

import java.util.function.Supplier;

public final class ItemModelProvider extends ModelProvider<ItemModelBuilder>
{
    @ApiStatus.Internal
    public ItemModelProvider(PackOutput packOutput, String modId)
    {
        super(packOutput, modId, Registries.ITEM, ItemModelBuilder::new);
    }

    // region: BlockItem
    public ItemModelBuilder blockItem(Item item, ResourceLocation blockName)
    {
        return blockItem(BuiltInRegistries.ITEM.getKey(item), blockName);
    }

    public ItemModelBuilder blockItem(ItemLike item, ResourceLocation blockName)
    {
        return blockItem(item.asItem(), blockName);
    }

    public ItemModelBuilder blockItem(Supplier<? extends ItemLike> item, ResourceLocation blockName)
    {
        return blockItem(item.get(), blockName);
    }

    public ItemModelBuilder blockItem(ResourceLocation itemName, ResourceLocation blockName)
    {
        return withParent(extendWithFolder(itemName), extendWith(blockName, "block/"));
    }

    public ItemModelBuilder blockItem(Item item)
    {
        var itemName = BuiltInRegistries.ITEM.getKey(item);
        return blockItem(itemName, itemName);
    }

    public ItemModelBuilder blockItem(ItemLike item)
    {
        return blockItem(item.asItem());
    }

    public ItemModelBuilder blockItem(Supplier<? extends ItemLike> item)
    {
        return blockItem(item.get());
    }

    public ItemModelBuilder blockItem(ResourceLocation itemName)
    {
        return blockItem(itemName, itemName);
    }
    // endregion

    // region: BasicItem
    public ItemModelBuilder basicItem(Item item)
    {
        return basicItem(BuiltInRegistries.ITEM.getKey(item));
    }

    public ItemModelBuilder basicItem(ItemLike item)
    {
        return basicItem(item.asItem());
    }

    public ItemModelBuilder basicItem(Supplier<? extends ItemLike> item)
    {
        return basicItem(item.get());
    }

    public ItemModelBuilder basicItem(ResourceLocation itemName)
    {
        return withParent(extendWithFolder(itemName), new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, "item/generated"))
                .texture(TextureSlot.LAYER0, extendWithFolder(itemName))
        ;
    }
    // endregion

    @Override
    protected void registerModels()
    {
        Generators.processDataGenerator(modId, ProviderTypes.ITEM_MODELS, this);
    }

    @Override
    public String getName()
    {
        return "ItemModels";
    }
}
